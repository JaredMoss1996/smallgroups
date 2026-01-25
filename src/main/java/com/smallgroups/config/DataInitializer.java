package com.smallgroups.config;

import com.smallgroups.model.SmallGroup;
import com.smallgroups.repository.SmallGroupRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    
    @Bean
    CommandLineRunner initDatabase(SmallGroupRepository repository) {
        return args -> {
            // Create sample small groups
            SmallGroup group1 = new SmallGroup();
            group1.setName("Young Professionals Bible Study");
            group1.setChurchName("First Baptist Church");
            group1.setDenomination("Baptist");
            group1.setLocation("Birmingham, AL");
            group1.setAddress("2001 Park Place, Birmingham, AL 35203");
            group1.setLatitude(33.5186);
            group1.setLongitude(-86.8104);
            group1.setDescription("A dynamic group for young professionals to study God's Word and build community.");
            group1.setAgeGroup("26-35");
            group1.setGender("Co-Ed");
            group1.setType("Bible Study");
            group1.setMeetingDay("Wednesday");
            group1.setMeetingTime("19:00");
            group1.setChildcareIncluded(false);
            group1.setHandicapAccessible(true);
            group1.setContactName("John Smith");
            group1.setContactEmail("john@example.com");
            group1.setContactPhone("(205) 555-0100");
            group1.setCurrentSize(8);
            group1.setMaxSize(12);
            
            SmallGroup group2 = new SmallGroup();
            group2.setName("Women of Faith");
            group2.setChurchName("Grace Community Church");
            group2.setDenomination("Non-Denominational");
            group2.setLocation("Mountain Brook, AL");
            group2.setAddress("3456 Mountain Lane, Mountain Brook, AL 35223");
            group2.setLatitude(33.4843);
            group2.setLongitude(-86.7459);
            group2.setDescription("A welcoming group for women to grow in faith and friendship.");
            group2.setAgeGroup("All Ages");
            group2.setGender("Women Only");
            group2.setType("Bible Study");
            group2.setMeetingDay("Tuesday");
            group2.setMeetingTime("10:00");
            group2.setChildcareIncluded(true);
            group2.setHandicapAccessible(true);
            group2.setContactName("Sarah Johnson");
            group2.setContactEmail("sarah@example.com");
            group2.setContactPhone("(205) 555-0200");
            group2.setCurrentSize(15);
            group2.setMaxSize(20);
            
            SmallGroup group3 = new SmallGroup();
            group3.setName("College Ministry");
            group3.setChurchName("Highlands Church");
            group3.setDenomination("Non-Denominational");
            group3.setLocation("Birmingham, AL");
            group3.setAddress("1000 Campus Drive, Birmingham, AL 35233");
            group3.setLatitude(33.5125);
            group3.setLongitude(-86.7995);
            group3.setDescription("A vibrant community for college students to connect and grow in faith.");
            group3.setAgeGroup("18-25");
            group3.setGender("Co-Ed");
            group3.setType("Social");
            group3.setMeetingDay("Thursday");
            group3.setMeetingTime("20:00");
            group3.setChildcareIncluded(false);
            group3.setHandicapAccessible(true);
            group3.setContactName("Mike Davis");
            group3.setContactEmail("mike@example.com");
            group3.setContactPhone("(205) 555-0300");
            group3.setCurrentSize(25);
            group3.setMaxSize(30);
            
            SmallGroup group4 = new SmallGroup();
            group4.setName("Men's Prayer Group");
            group4.setChurchName("St. Paul's Methodist Church");
            group4.setDenomination("Methodist");
            group4.setLocation("Homewood, AL");
            group4.setAddress("1800 Oxmoor Road, Homewood, AL 35209");
            group4.setLatitude(33.4687);
            group4.setLongitude(-86.7947);
            group4.setDescription("Men gathering to pray and support one another in their faith journey.");
            group4.setAgeGroup("36-45");
            group4.setGender("Men Only");
            group4.setType("Prayer Group");
            group4.setMeetingDay("Saturday");
            group4.setMeetingTime("07:00");
            group4.setChildcareIncluded(false);
            group4.setHandicapAccessible(true);
            group4.setContactName("Tom Anderson");
            group4.setContactEmail("tom@example.com");
            group4.setContactPhone("(205) 555-0400");
            group4.setCurrentSize(10);
            group4.setMaxSize(15);
            
            SmallGroup group5 = new SmallGroup();
            group5.setName("Family Discipleship");
            group5.setChurchName("Hope Presbyterian Church");
            group5.setDenomination("Presbyterian");
            group5.setLocation("Vestavia Hills, AL");
            group5.setAddress("2200 Columbiana Road, Vestavia Hills, AL 35216");
            group5.setLatitude(33.4484);
            group5.setLongitude(-86.7872);
            group5.setDescription("A group for families to grow together in their relationship with Christ.");
            group5.setAgeGroup("All Ages");
            group5.setGender("Co-Ed");
            group5.setType("Discipleship");
            group5.setMeetingDay("Sunday");
            group5.setMeetingTime("17:00");
            group5.setChildcareIncluded(true);
            group5.setHandicapAccessible(true);
            group5.setContactName("Lisa Brown");
            group5.setContactEmail("lisa@example.com");
            group5.setContactPhone("(205) 555-0500");
            group5.setCurrentSize(12);
            group5.setMaxSize(18);
            
            SmallGroup group6 = new SmallGroup();
            group6.setName("Senior Saints Fellowship");
            group6.setChurchName("Community Bible Church");
            group6.setDenomination("Baptist");
            group6.setLocation("Hoover, AL");
            group6.setAddress("3500 Stadium Trace Parkway, Hoover, AL 35244");
            group6.setLatitude(33.3762);
            group6.setLongitude(-86.8117);
            group6.setDescription("A community for seniors to fellowship and study Scripture together.");
            group6.setAgeGroup("56+");
            group6.setGender("Co-Ed");
            group6.setType("Bible Study");
            group6.setMeetingDay("Monday");
            group6.setMeetingTime("14:00");
            group6.setChildcareIncluded(false);
            group6.setHandicapAccessible(true);
            group6.setContactName("Robert Williams");
            group6.setContactEmail("robert@example.com");
            group6.setContactPhone("(205) 555-0600");
            group6.setCurrentSize(20);
            group6.setMaxSize(25);
            
            repository.save(group1);
            repository.save(group2);
            repository.save(group3);
            repository.save(group4);
            repository.save(group5);
            repository.save(group6);
            
            System.out.println("Sample data initialized successfully!");
        };
    }
}
