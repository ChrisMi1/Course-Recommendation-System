package com.uniwa.course_recommendation.service;

import com.uniwa.course_recommendation.dto.AnswerDto;
import com.uniwa.course_recommendation.entity.AnswerLabels;
import com.uniwa.course_recommendation.entity.Course;
import com.uniwa.course_recommendation.entity.CourseChoices;
import com.uniwa.course_recommendation.repo.AbstractRepository;
import com.uniwa.course_recommendation.repo.AnswerLabelRepository;
import com.uniwa.course_recommendation.repo.CourseRepository;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DesicionTreeService {
    Logger logger = LoggerFactory.getLogger(AbstractRepository.class);
    @Autowired
    AnswerLabelRepository answerLabelRepository;
    @Autowired
    CourseRepository courseRepository;

    public enum Level {
        FLOW,
        SPECIALIZATION,
        INTERESTS

    }
    @Getter
    @Setter
    private static class CourseFeatures {
        private String interest;
        private Course course;
        private boolean duplicate;
        private boolean flow;
        private int score;
        private boolean isAdded;

        public CourseFeatures() {
            duplicate = false;
            flow = false;
            score = 0;
            isAdded = false;
        }
    }


    public Set<Course> mapUserAnswersToCourses(List<AnswerDto> answers) {
        HashMap<Level, String> labels = new HashMap<>();
        Set<Course> recommendedCourses = new HashSet<>();

        Set<String> distinctLabelsForInterests = getLabelsForInterests(answers.get(2).getAnswer());

        labels.put(Level.INTERESTS,String.join(",",distinctLabelsForInterests));

        if (answers.get(0).getAnswer().equalsIgnoreCase("Ροή Λογισμικού και Πληροφοριακών Συστημάτων")) {
            labels.put(Level.FLOW,"Flow Software");
            if (answers.get(1).getAnswer().equalsIgnoreCase("Άναπτυξη διαδικτυακών εφαρμογών")) {
                labels.put(Level.SPECIALIZATION,"Software Engineering,Information Systems");
                findCoreCourses(recommendedCourses,"Άναπτυξη διαδικτυακών εφαρμογών");
                //check if user choose 1 selection from the question with multiple answers
                if (answers.get(2).getAnswer().split(",").length <= 1 ) {
                    logger.info("User gave only one selection in 3rd question");
                    findCoursesFromInterests(recommendedCourses,"Άναπτυξη διαδικτυακών εφαρμογών",answers.get(2).getAnswer());
                } else {
                    logger.info("User choose more than one selection in 3rd question");
                    findCoursesBasedOnScoreFromInterests(labels,"Flow Software","Άναπτυξη διαδικτυακών εφαρμογών",answers.get(2).getAnswer(),recommendedCourses);
                }
            } else if (answers.get(1).getAnswer().equalsIgnoreCase("Ανάπτυξη παιχνιδιών")) {
                labels.put(Level.SPECIALIZATION,"Game Development");
                findCoreCourses(recommendedCourses,"Ανάπτυξη παιχνιδιών");
                //check if user choose 1 selection from the question with multiple answers

                if (answers.get(2).getAnswer().split(",").length <= 1 ) {
                    logger.info("User gave only one selection in 3rd question");
                    findCoursesFromInterests(recommendedCourses,"Ανάπτυξη παιχνιδιών",answers.get(2).getAnswer());
                } else {
                    logger.info("User choose more than one selection in 3rd question");

                    findCoursesBasedOnScoreFromInterests(labels,"Flow Software","Ανάπτυξη παιχνιδιών",answers.get(2).getAnswer(),recommendedCourses);
                }
            } else if (answers.get(1).getAnswer().equalsIgnoreCase("Ανάπτυξη κινητής τηλεφωνίας")) {
                labels.put(Level.SPECIALIZATION,"Mobile Development,Android Development,Software Engineering");
                findCoreCourses(recommendedCourses,"Ανάπτυξη κινητής τηλεφωνίας");
                if (answers.get(2).getAnswer().split(",").length <= 1 ) {
                    logger.info("User gave only one selection in 3rd question");
                    findCoursesFromInterests(recommendedCourses,"Ανάπτυξη κινητής τηλεφωνίας",answers.get(2).getAnswer());
                } else {
                    logger.info("User choose more than one selection in 3rd question");

                    findCoursesBasedOnScoreFromInterests(labels,"Flow Software","Ανάπτυξη κινητής τηλεφωνίας",answers.get(2).getAnswer(),recommendedCourses);
                }
            } else {
                labels.put(Level.SPECIALIZATION,"AI & Machine Learning,Data");
                findCoreCourses(recommendedCourses,"Τεχνητή νοημοσύνη και μηχανική μάθηση");
                if (answers.get(2).getAnswer().split(",").length <= 1 ) {
                    logger.info("User gave only one selection in 3rd question");
                    findCoursesFromInterests(recommendedCourses,"Τεχνητή νοημοσύνη και μηχανική μάθηση",answers.get(2).getAnswer());
                    return recommendedCourses;
                } else {
                    logger.info("User choose more than one selection in 3rd question");

                    findCoursesBasedOnScoreFromInterests(labels,"Flow Software","Τεχνητή νοημοσύνη και μηχανική μάθηση",answers.get(2).getAnswer(),recommendedCourses);
                }
            }

        } else if (answers.get(0).getAnswer().equalsIgnoreCase("Ροή Υλικού και Υπολογιστικών Συστημάτων")) {
            labels.put(Level.FLOW,"Flow Hardware");

            if (answers.get(1).getAnswer().equalsIgnoreCase("Ενσωματωμένα Συστήματα")) {
                labels.put(Level.SPECIALIZATION,"Embedded Systems,Hardware");
                findCoreCourses(recommendedCourses,"Ενσωματωμένα Συστήματα");
                if (answers.get(2).getAnswer().split(",").length <= 1 ) {
                    logger.info("User gave only one selection in 3rd question");
                    findCoursesFromInterests(recommendedCourses,"Ενσωματωμένα Συστήματα",answers.get(2).getAnswer());
                } else {
                    logger.info("User choose more than one selection in 3rd question");

                    findCoursesBasedOnScoreFromInterests(labels,"Flow Hardware","Ενσωματωμένα Συστήματα",answers.get(2).getAnswer(),recommendedCourses);
                }

            } else if (answers.get(1).getAnswer().equalsIgnoreCase("Ρομποτική")) {
                labels.put(Level.SPECIALIZATION,"Robotics,Embedded Systems, Hardware");
                findCoreCourses(recommendedCourses,"Ρομποτική");
                if (answers.get(2).getAnswer().split(",").length <= 1 ) {
                    logger.info("User gave only one selection in 3rd question");
                    findCoursesFromInterests(recommendedCourses,"Ρομποτική",answers.get(2).getAnswer());
                } else {
                    logger.info("User choose more than one selection in 3rd question");

                    findCoursesBasedOnScoreFromInterests(labels,"Flow Hardware","Ρομποτική",answers.get(2).getAnswer(),recommendedCourses);
                }
            } else if (answers.get(1).getAnswer().equalsIgnoreCase("Μικροϋπολογιστές")) {
                labels.put(Level.SPECIALIZATION,"Embedded Systems, Hardware");
                findCoreCourses(recommendedCourses,"Μικροϋπολογιστές");
                if (answers.get(2).getAnswer().split(",").length <= 1 ) {
                    logger.info("User gave only one selection in 3rd question");
                    findCoursesFromInterests(recommendedCourses,"Μικροϋπολογιστές",answers.get(2).getAnswer());
                } else {
                    logger.info("User choose more than one selection in 3rd question");
                    findCoursesBasedOnScoreFromInterests(labels,"Flow Hardware","Μικροϋπολογιστές",answers.get(2).getAnswer(),recommendedCourses);
                }
            } else {
                labels.put(Level.SPECIALIZATION,"Hardware,Σχεδιασμός VLSI");
                findCoreCourses(recommendedCourses,"Σχεδιασμός VLSI");
                if (answers.get(2).getAnswer().split(",").length <= 1 ) {
                    logger.info("User gave only one selection in 3rd question");
                    findCoursesFromInterests(recommendedCourses,"Σχεδιασμός VLSI",answers.get(2).getAnswer());
                } else {
                    logger.info("User choose more than one selection in 3rd question");

                    findCoursesBasedOnScoreFromInterests(labels,"Flow Hardware","Σχεδιασμός VLSI",answers.get(2).getAnswer(),recommendedCourses);
                }
            }

        } else {
            labels.put(Level.FLOW,"Flow Network");

            if (answers.get(1).getAnswer().equalsIgnoreCase("Σχεδιασμός Δικτύου")) {
                labels.put(Level.SPECIALIZATION,"Networking,Network Protocols");
                findCoreCourses(recommendedCourses,"Σχεδιασμός Δικτύου");
                if (answers.get(2).getAnswer().split(",").length <= 1 ) {
                    logger.info("User gave only one selection in 3rd question");
                    findCoursesFromInterests(recommendedCourses,"Σχεδιασμός Δικτύου",answers.get(2).getAnswer());
                } else {
                    logger.info("User choose more than one selection in 3rd question");

                    findCoursesBasedOnScoreFromInterests(labels,"Flow Network","Σχεδιασμός Δικτύου",answers.get(2).getAnswer(),recommendedCourses);
                }

            } else if (answers.get(1).getAnswer().equalsIgnoreCase("Ασφάλεια Δικτύου")) {
                labels.put(Level.SPECIALIZATION,"Cybersecurity,Networking");
                findCoreCourses(recommendedCourses,"Ασφάλεια Δικτύου");
                if (answers.get(2).getAnswer().split(",").length <= 1 ) {
                    logger.info("User gave only one selection in 3rd question");
                    findCoursesFromInterests(recommendedCourses,"Ασφάλεια Δικτύου",answers.get(2).getAnswer());
                } else {
                    logger.info("User choose more than one selection in 3rd question");

                    findCoursesBasedOnScoreFromInterests(labels,"Flow Network","Ασφάλεια Δικτύου",answers.get(2).getAnswer(),recommendedCourses);
                }
            } else if (answers.get(1).getAnswer().equalsIgnoreCase("Ασύρματα Δίκτυα και IoT")) {
                labels.put(Level.SPECIALIZATION,"Wireless Networks,IoT,Networking");
                findCoreCourses(recommendedCourses,"Ασύρματα Δίκτυα και IoT");
                if (answers.get(2).getAnswer().split(",").length <= 1 ) {
                    logger.info("User gave only one selection in 3rd question");
                    findCoursesFromInterests(recommendedCourses,"Ασύρματα Δίκτυα και IoT",answers.get(2).getAnswer());
                } else {
                    logger.info("User choose more than one selection in 3rd question");

                    findCoursesBasedOnScoreFromInterests(labels,"Flow Network","Ασύρματα Δίκτυα και IoT",answers.get(2).getAnswer(),recommendedCourses);
                }
            } else {
                labels.put(Level.SPECIALIZATION,"Telecommunications,Networking");
                findCoreCourses(recommendedCourses,"Τηλεπικοινωνίες");
                if (answers.get(2).getAnswer().split(",").length <= 1 ) {
                    logger.info("User gave only one selection in 3rd question");
                    findCoursesFromInterests(recommendedCourses,"Τηλεπικοινωνίες",answers.get(2).getAnswer());
                } else {
                    logger.info("User choose more than one selection in 3rd question");
                    findCoursesBasedOnScoreFromInterests(labels,"Flow Network","Τηλεπικοινωνίες",answers.get(2).getAnswer(),recommendedCourses);
                }
            }
        }
        return recommendedCourses;
    }

    private Set<String> getLabelsForInterests(String answer) {
        List<String> answers = new ArrayList<>(List.of(answer.split(",")));
        List<AnswerLabels> answerLabels = answerLabelRepository.getAnswerLabelsByAnswers(answers);
        Set<String> distinctAnswerLabels = new HashSet<>();
        for (AnswerLabels ans : answerLabels) {
            distinctAnswerLabels.addAll(new HashSet<>(List.of(ans.getLabels().split(","))));
        }
        return distinctAnswerLabels;
    }

    private void findCoreCourses(Set<Course> recommendedCourses,String specialization) {
        List<CourseChoices> courseChoices = courseRepository.findCoreCourses(specialization);
        for (CourseChoices cc : courseChoices) {
            List<String> courseChoicesNames = new ArrayList<>(List.of(cc.getCourses().split(",")));
            for (String name : courseChoicesNames) {
                Course course = courseRepository.findCourseByName(name);
                if (course == null) logger.info("This course wasn't found " + name);
                else recommendedCourses.add(course);
            }
        }
    }

    private void findCoursesFromInterests(Set<Course> recommendedCourses,String answer1,String answer2) {
        CourseChoices courseChoices = courseRepository.findCoursesFromInterests(answer1,answer2);
        List<String> courseChoicesNames = new ArrayList<>(List.of(courseChoices.getCourses().split(",")));
        for (String name : courseChoicesNames) {
            Course course = courseRepository.findCourseByName(name);
            if (course == null) logger.info("This course wasn't found " + name);
            else recommendedCourses.add(course);
        }
    }


    private void findCoursesBasedOnScoreFromInterests(HashMap<Level, String> labels,String flow,String specialization,String interests,Set<Course> recommended) {
        int totalRecommended = recommended.size();
        //count from recommended courses how many they belong to stream
        int coursesCountOfSameFlow = (int) recommended.stream().filter(rec -> rec.getLabels().contains(flow)).count();
        //Get the courses for every combination(specialization and interest)
        Map<String, List<Course>> potentialCourses = findThePotentialCourses(specialization, interests);
        List<CourseFeatures> courseFeaturesList = calculateScores(potentialCourses,flow,labels);
        //We do that for circular indexing
        List<String> interestsOfUser = new ArrayList<>(List.of(interests.split(",")));
        int numInterests = potentialCourses.size();
        //Sort elements based on their score
        List<CourseFeatures> coursesFromInterestSorted = courseFeaturesList.stream().sorted(Comparator.comparingInt(CourseFeatures::getScore).reversed()).toList();
        int tmp = totalRecommended;
        for (int i = 0; i < (12 - tmp); i++) {
            String currentInterest = interestsOfUser.get(i % numInterests);
            if (coursesCountOfSameFlow < 5) {
                //check if the rest courses that we have to add must be from the flow
                if ((5 - coursesCountOfSameFlow) + totalRecommended == 12) {
                    List<CourseFeatures> filteredList = coursesFromInterestSorted.stream().filter(cf -> cf.getInterest().equalsIgnoreCase(currentInterest) && !cf.isAdded() && cf.isFlow()).toList();
                    for (var courseFromInterest : filteredList) {

                        if (!recommended.contains(courseFromInterest.getCourse())) {
                            recommended.add(courseFromInterest.getCourse());
                            totalRecommended++;
                            coursesCountOfSameFlow++;
                            courseFromInterest.setAdded(true);
                            break;
                        }
                    }
                    continue;
                }
            }
            List<CourseFeatures> filteredList = coursesFromInterestSorted.stream().filter(cf -> cf.getInterest().equalsIgnoreCase(currentInterest) && !cf.isAdded()).toList();
            //Iterate the list until you find the course with the highest score that doesn't exist in the recommended list
            for (var courseFromInterest : filteredList) {
                if (!recommended.contains(courseFromInterest.getCourse())) {
                    if (courseFromInterest.isFlow()) {
                        coursesCountOfSameFlow++;
                    }
                    recommended.add(courseFromInterest.getCourse());
                    totalRecommended++;
                    courseFromInterest.setAdded(true);
                    //Continue to the next interest
                    break;
                }
            }
        }
    }

    private List<CourseFeatures> calculateScores(Map<String, List<Course>> potentialCourses,String flow,Map<Level, String> labels) {
        List<CourseFeatures> courseFeaturesList = new ArrayList<>();
        //Find the scores for every course
        for (var pot : potentialCourses.entrySet()) {
            //TODO: ADD ALSO DUPLICATES FUNCTIONALITY
            for (Course course : pot.getValue()) {
                CourseFeatures courseFeatures = new CourseFeatures();
                courseFeatures.setInterest(pot.getKey());
                courseFeatures.setCourse(course);
                if (course.getLabels().contains(flow)) {
                    courseFeatures.setScore(courseFeatures.getScore() + 2);
                    courseFeatures.setFlow(true);
                }
                boolean matchesInterests = Arrays.stream(labels.get(Level.INTERESTS).split(",")).anyMatch(label -> course.getLabels().contains(label));
                if (matchesInterests) {
                    courseFeatures.setScore(courseFeatures.getScore() + 5);

//                    if (courseFeatures.isFlow()) {
//                        //if it is in the same flow +5 pts
//                        courseFeatures.setScore(courseFeatures.getScore() + 5);
//                    } else {
//                        //if it is not in the same flow +3 pts
//                        courseFeatures.setScore(courseFeatures.getScore() + 3);
//                    }
                    if (!courseFeatures.isFlow()) {
                        //if it is in the same flow +5 pts
                        courseFeatures.setScore(courseFeatures.getScore() + 2);
                    }
                }
                boolean matchesSpecialization = Arrays.stream(labels.get(Level.SPECIALIZATION).split(",")).anyMatch(label -> course.getLabels().contains(label));
                if (matchesSpecialization) {
                    courseFeatures.setScore(courseFeatures.getScore() + 2);

//                    if (courseFeatures.isFlow()) {
//                        //if it is in the same flow +2 pts
//                        courseFeatures.setScore(courseFeatures.getScore() + 2);
//                    } else {
//                        //if it is in the same flow +1 pts
//                        courseFeatures.setScore(courseFeatures.getScore() + 1);
//                    }
                }
                courseFeaturesList.add(courseFeatures);
            }
        }
        return courseFeaturesList;
    }

    private Map<String,List<Course>> findThePotentialCourses(String specialization,String interests) {
        Map<String,List<Course>> potentialCourses = new HashMap<>();
        for (String interest : interests.split(",")) {
            CourseChoices courseChoices = courseRepository.findCoursesFromInterests(specialization,interest);
            List<String> courseChoicesNames = new ArrayList<>(List.of(courseChoices.getCourses().split(",")));
            List<Course> extractedCourses = new ArrayList<>();
            for (String name : courseChoicesNames) {
                Course course = courseRepository.findCourseByName(name);
                extractedCourses.add(course);
            }
            potentialCourses.put(interest,extractedCourses);
        }
        return potentialCourses;
    }
}
