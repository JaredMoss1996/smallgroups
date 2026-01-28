package com.smallgroups.controller;

import com.smallgroups.model.Church;
import com.smallgroups.model.SmallGroup;
import com.smallgroups.model.User;
import com.smallgroups.service.ApprovalRequestService;
import com.smallgroups.service.ChurchService;
import com.smallgroups.service.CustomUserDetailsService;
import com.smallgroups.service.SmallGroupService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebController {
    
    private final SmallGroupService groupService;
    private final CustomUserDetailsService userDetailsService;
    private final ChurchService churchService;
    private final ApprovalRequestService approvalRequestService;
    
    public WebController(SmallGroupService groupService, 
                        CustomUserDetailsService userDetailsService,
                        ChurchService churchService,
                        ApprovalRequestService approvalRequestService) {
        this.groupService = groupService;
        this.userDetailsService = userDetailsService;
        this.churchService = churchService;
        this.approvalRequestService = approvalRequestService;
    }
    
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("churches", churchService.getAllChurches());
        return "signup";
    }
    
    @PostMapping("/signup/submit")
    public String signupSubmit(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam(required = false) Long homeChurchId,
            Model model) {
        
        // Validate password match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "fragments/signup-fragments :: error(message='Passwords do not match')";
        }
        
        try {
            userDetailsService.registerUser(email, password, name, homeChurchId);
            return "fragments/signup-fragments :: success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "fragments/signup-fragments :: error(message='" + e.getMessage() + "')";
        }
    }
    
    @GetMapping("/find")
    public String findGroups(Model model, Authentication authentication) {
        // Load initial state
        String email = authentication.getName();
        User user = userDetailsService.getUserByEmail(email);
        model.addAttribute("currentUser", user);
        return "find-groups";
    }
    
    @GetMapping("/groups/search")
    public String searchGroups(
            @RequestParam(required = false) String churchName,
            @RequestParam(required = false) String denomination,
            @RequestParam(required = false) String ageGroup,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean childcareIncluded,
            @RequestParam(required = false) Boolean handicapAccessible,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) Double userLat,
            @RequestParam(required = false) Double userLon,
            Model model,
            Authentication authentication) {
        
        List<SmallGroup> groups = groupService.searchGroups(churchName, denomination, 
                ageGroup, gender, type, location, childcareIncluded, handicapAccessible, 
                radius, userLat, userLon);
        
        // Check membership for each group
        if (authentication != null) {
            String email = authentication.getName();
            User user = userDetailsService.getUserByEmail(email);
            if (user != null) {
                groups.forEach(group -> {
                    group.setIsMember(groupService.isUserMember(user.getId(), group.getId()));
                });
            }
        }
        
        model.addAttribute("groups", groups);
        return "fragments/search-results :: search-results(groups=${groups})";
    }
    
    @PostMapping("/groups/{id}/join")
    public String joinGroup(@PathVariable Long id, Model model, Authentication authentication) {
        String email = authentication.getName();
        User user = userDetailsService.getUserByEmail(email);
        
        if (user != null) {
            groupService.joinGroup(user.getId(), id);
        }
        
        // Return the updated group card
        SmallGroup group = groupService.getGroupById(id).orElse(null);
        if (group != null && user != null) {
            group.setIsMember(groupService.isUserMember(user.getId(), group.getId()));
        }
        model.addAttribute("group", group);
        model.addAttribute("showJoinButton", true);
        return "fragments/group-card :: group-card(group=${group}, showJoinButton=${showJoinButton})";
    }
    
    @PostMapping("/groups/{id}/leave")
    public String leaveGroup(@PathVariable Long id, Model model, Authentication authentication) {
        String email = authentication.getName();
        User user = userDetailsService.getUserByEmail(email);
        
        if (user != null) {
            groupService.leaveGroup(user.getId(), id);
        }
        
        // Return the updated group card
        SmallGroup group = groupService.getGroupById(id).orElse(null);
        if (group != null && user != null) {
            group.setIsMember(groupService.isUserMember(user.getId(), group.getId()));
        }
        model.addAttribute("group", group);
        model.addAttribute("showJoinButton", true);
        return "fragments/group-card :: group-card(group=${group}, showJoinButton=${showJoinButton})";
    }
    
    @GetMapping("/create")
    public String createGroup(Model model, Authentication authentication) {
        String email = authentication.getName();
        User user = userDetailsService.getUserByEmail(email);
        
        // Check if user is approved or admin
        boolean canCreate = user != null && 
                           (user.getApprovedForGroupCreation() || 
                            "ADMIN".equals(user.getRole()) || 
                            "GROUP_CREATOR".equals(user.getRole()));
        
        model.addAttribute("canCreate", canCreate);
        model.addAttribute("churches", churchService.getAllChurches());
        model.addAttribute("currentUser", user);
        return "create-group";
    }
    
    @PostMapping("/groups/create")
    public String createGroupSubmit(
            @RequestParam String name,
            @RequestParam String churchName,
            @RequestParam(required = false) String denomination,
            @RequestParam String location,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String imageUrl,
            @RequestParam(required = false) String ageGroup,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String meetingDay,
            @RequestParam(required = false) String meetingTime,
            @RequestParam(required = false) Boolean childcareIncluded,
            @RequestParam(required = false) Boolean handicapAccessible,
            @RequestParam(required = false) String contactName,
            @RequestParam(required = false) String contactEmail,
            @RequestParam(required = false) String contactPhone,
            @RequestParam(required = false) Integer currentSize,
            @RequestParam(required = false) Integer maxSize,
            Model model,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userDetailsService.getUserByEmail(email);
        
        if (user == null) {
            model.addAttribute("error", "User not found");
            return "fragments/approval-request :: approval-error(message='User not found')";
        }
        
        // Check permissions
        if (!user.getApprovedForGroupCreation() && 
            !"ADMIN".equals(user.getRole()) && 
            !"GROUP_CREATOR".equals(user.getRole())) {
            model.addAttribute("error", "You must be approved by your home church to create groups");
            return "fragments/approval-request :: approval-error(message='You must be approved by your home church to create groups')";
        }
        
        // Create the group
        SmallGroup group = new SmallGroup();
        group.setName(name);
        group.setChurchName(churchName);
        group.setDenomination(denomination);
        group.setLocation(location);
        group.setAddress(address);
        group.setLatitude(latitude);
        group.setLongitude(longitude);
        group.setDescription(description);
        group.setImageUrl(imageUrl);
        group.setAgeGroup(ageGroup);
        group.setGender(gender);
        group.setType(type);
        group.setMeetingDay(meetingDay);
        group.setMeetingTime(meetingTime);
        group.setChildcareIncluded(childcareIncluded != null && childcareIncluded);
        group.setHandicapAccessible(handicapAccessible != null && handicapAccessible);
        group.setContactName(contactName);
        group.setContactEmail(contactEmail);
        group.setContactPhone(contactPhone);
        group.setCurrentSize(currentSize);
        group.setMaxSize(maxSize);
        group.setCreatorId(user.getId());
        
        try {
            groupService.createGroup(group);
            return "fragments/create-group-success :: success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "fragments/approval-request :: approval-error(message='" + e.getMessage() + "')";
        }
    }
    
    @PostMapping("/approval-request/send")
    public String sendApprovalRequest(
            @RequestParam Long churchId,
            @RequestParam String message,
            Model model,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userDetailsService.getUserByEmail(email);
        
        if (user == null) {
            model.addAttribute("message", "User not found");
            return "fragments/approval-request :: approval-error(message='User not found')";
        }
        
        try {
            approvalRequestService.createRequest(user.getId(), churchId, message);
            return "fragments/approval-request :: approval-success";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "fragments/approval-request :: approval-error(message='" + e.getMessage() + "')";
        }
    }
    
    @GetMapping("/joined-groups")
    public String joinedGroups(Model model, Authentication authentication) {
        String email = authentication.getName();
        User user = userDetailsService.getUserByEmail(email);
        
        if (user != null) {
            List<SmallGroup> groups = groupService.getJoinedGroups(user.getId());
            groups.forEach(group -> group.setIsMember(true));
            model.addAttribute("groups", groups);
        } else {
            model.addAttribute("groups", List.of());
        }
        
        return "joined-groups";
    }
    
    @GetMapping("/joined-groups/list")
    public String joinedGroupsList(Model model, Authentication authentication) {
        String email = authentication.getName();
        User user = userDetailsService.getUserByEmail(email);
        
        if (user != null) {
            List<SmallGroup> groups = groupService.getJoinedGroups(user.getId());
            groups.forEach(group -> group.setIsMember(true));
            model.addAttribute("groups", groups);
        } else {
            model.addAttribute("groups", List.of());
        }
        
        return "fragments/joined-groups-list :: joined-groups-list(groups=${groups})";
    }
}
