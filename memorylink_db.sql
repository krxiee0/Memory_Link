-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 10, 2026 at 11:48 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `memorylink_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `flashcards`
--

CREATE TABLE `flashcards` (
  `id` bigint(20) NOT NULL,
  `answer` varchar(5000) DEFAULT NULL,
  `difficulty` varchar(255) DEFAULT NULL,
  `question` varchar(5000) DEFAULT NULL,
  `strand` varchar(255) DEFAULT NULL,
  `lesson` varchar(255) DEFAULT NULL,
  `lesson_number` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `flashcards`
--

INSERT INTO `flashcards` (`id`, `answer`, `difficulty`, `question`, `strand`, `lesson`, `lesson_number`) VALUES
(2, 'Recall Question Type', 'Medium', 'A survey question type that asks respondents to provide specific retrieved information', 'STEM B', 'ENGLISH 201', 'Lesson 1'),
(3, 'Experiment', NULL, 'A research method performed in a laboratory or natural setting to determine cause and effect', 'STEM B', 'ENGLISH 201', 'Lesson 1'),
(4, 'Observation', NULL, 'A research instrument that allows the description of behaviour in a naturalistic or laboratory setting', 'STEM B', 'ENGLISH 201', 'Lesson 1'),
(5, 'Survey', NULL, 'A research instrument containing prepared questions used to measure attitudes, perceptions, and opinions', 'STEM B', 'ENGLISH 201', 'Lesson 1'),
(6, 'Research Instrument', NULL, 'A tool or device used to collect, measure, and analyze data to answer research questions or test hypotheses.', 'STEM B', 'ENGLISH 201', 'Lesson 1'),
(7, 'POWER', NULL, 'The Capacity to influence the actions, behaviors, and decisions of another\nindividual.', 'STEM A', 'SOC 101', 'Lesson 1'),
(8, 'Force (as a source of power)', NULL, 'The actual use of power by threatening coercion or consequences to impose an action or decision over another.', 'STEM A', 'SOC 101', 'Lesson 1'),
(9, 'Influence (as a source of power)', NULL, 'The ability to modify another person’s behavior and decision-making.', 'STEM A', 'SOC 101', 'Lesson 1'),
(10, 'Authority', NULL, 'Power generated from legitimate means, such as an election.', 'STEM A', 'SOC 101', 'Lesson 1'),
(11, 'Traditional Authority', NULL, 'Absolute power passed on from generation to generation.', 'STEM A', 'SOC 101', 'Lesson 1');

-- --------------------------------------------------------

--
-- Table structure for table `lessons`
--

CREATE TABLE `lessons` (
  `id` bigint(20) NOT NULL,
  `category` varchar(255) DEFAULT NULL,
  `description` varchar(3000) DEFAULT NULL,
  `duration` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `topics` int(11) DEFAULT NULL,
  `strand` varchar(255) DEFAULT NULL,
  `approved_at` varchar(255) DEFAULT NULL,
  `approved_by` varchar(255) DEFAULT NULL,
  `rejected_at` varchar(255) DEFAULT NULL,
  `rejected_by` varchar(255) DEFAULT NULL,
  `rejection_reason` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `sub_lessons` text DEFAULT NULL,
  `submitted_at` varchar(255) DEFAULT NULL,
  `submitted_by` varchar(255) DEFAULT NULL,
  `updated_at` varchar(255) DEFAULT NULL,
  `number_of_lessons` int(11) DEFAULT NULL,
  `sub_lessons_json` longtext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `lessons`
--

INSERT INTO `lessons` (`id`, `category`, `description`, `duration`, `title`, `topics`, `strand`, `approved_at`, `approved_by`, `rejected_at`, `rejected_by`, `rejection_reason`, `status`, `sub_lessons`, `submitted_at`, `submitted_by`, `updated_at`, `number_of_lessons`, `sub_lessons_json`) VALUES
(38, 'STEM A', 'fefsefed', '30', 'SOC 101', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'approved', NULL, NULL, NULL, NULL, 2, '[{\"title\":\"Lesson 1\",\"content\":\"<h1 style=\\\"text-align: center;\\\">Power and Authority</h1><div><br></div><div><br></div><div><b>● POWER</b></div><div>- is the capacity to influence the actions, behaviors and decisions</div><div>of another individual.</div><div><br></div><div><b>(3) Sources of POWER:</b></div><div>1. Force - the actual use of power by threatening coercion or consequences to</div><div>Impose an action or decision over another.<br><br></div><div>2. Influence - means one can modify another person\'s behavior and decision making</div><div>using force.<br><br></div><div>3. Authority - is powered generated from legitimate means such as an election.</div><div><br></div><div><b>(3) Types of Authority:</b></div><div><ol><li>Traditional - absolute power passed on from generation to generation.</li><li>Rational - Legal - comes from the constitution of a leader, exercising this power are elected.</li><li>Charismatic - is power derived from the personality of a leader exuding charisma; is revolutionary and unstable.</li><li>The state as a social institution can be studied according to various sociological Perspectives:</li></ol></div><div><ul><li>FUNCTIONALIST</li><li>CONFLICT</li><li>INTERACTIONIST</li></ul></div><div><br></div><div><b>1. FUNCTIONALIST:</b></div><div>- it functions:</div><div><ol><li>) maintain peace and order;</li><li>) plan and direct the society;</li><li>) meet Social needs;</li><li>) manage international relations.</li></ol></div><div><br></div><div>• The goal of each state is to have a peaceful and better society, where people are</div><div>content with their lives.</div><div><br></div><div><b>2. CONFLICT PERSPECTIVE</b> - sees state as an all - controlling government that</div><div>constricts peoples\' movement to maintain the status quo.</div><div><br></div><div><b>3. INTERACTIONIST PERSPECTIVES</b> - a micro-level analysis of symbols that</div><div>maintain the status quo or how power is arranged by the government.</div><div>ex: the way politicians talk, act, &amp; make decisions.</div><div><br></div><div><b>POLITICAL ORGANIZATIONS:</b></div><div><ul><li>BAND - is the type of organization with lowest complexity and is an egalitarian</li></ul></div><div>society meaning, everyone is equal.</div><div>→ wealth is not accumulated by an individual, but rather shared with all its members.</div><div><ul><li>TRIBES - composed of segmentary lineages or clusters of family, and each lineage</li></ul></div><div>can compete for leadership in the tribe.</div><div>→ Laws in a tribe are not crafted to determine who is guilty as its goal is to resolve</div><div>conflict within the tribe.</div><div><br></div><div><ul><li><b>CHIEFDOM</b></li></ul></div><div>- is a ranked society, where social classes exist, along with the concept of Wealth and power.</div><div>→ Known to exercise economic change to reallocate and redistribute wealth from its</div><div>members to central authority.</div><div><br></div><div><ul><li><b>STATE</b></li></ul></div><div>- the largest and most formal of these organizations and is the most complex</div><div>as it runs on many levels of bureaucracies that handle various functions to meet its goals.</div><div>→ To achieve its goals, a state uses its power to direct its members and these powers</div><div>include taxation powers, eminent domain &amp; police powers.</div><div>→ The Philippines is guided by its constitution the “1987 Constitution”</div><div>It contains:</div><div>- The Laws of the Philippines</div><div>- It was crafted with the intention to prevent the rise of strongman authoritarian</div><div>leadership in the country.</div><div><br></div><div><ul><li><b>Executive Branch</b></li></ul></div><div>- Headed by the president, vice president, and the cabinet secretaries.</div><div>- Executed by the laws the legislative branch makes.</div><div>- Disburses the governments’ budget according to what it deems important.</div><div><br></div><div><ul><li><b>Legislative Branch</b></li></ul></div><div>- Composed of the Senate and House of Representatives</div><div>- Crafts laws that would benefit the country and ensures that the government is</div><div>working properly.</div><div><br></div><div><ul><li><b>Judiciary Branch</b></li></ul></div><div>- Headed by the Chief Justice and the members of the supreme court.</div><div>- Dispenses justice and ensures fundamental rights of the people are followed.</div><div>- Interprets the constitution and Laws of the Land.</div><div><br></div><div>- Checks and Balances: To prevent abuse of power and return of authoritarianism.</div><div>Example:</div><div>- The Legislative branch can impeach and remove high ranking officials</div><div><br></div><div><ul><li><b>Impeachment</b></li></ul></div><div>- A political process wherein the members of the House of Representatives decide to</div><div>initiate the removal of aforesaid officials, and the Senate acts as judges and decides</div><div>on the matter.</div><div><br></div><div><b>CONSTITUTIONAL COMMISIONS:</b></div><div>There are four (4) independent commissions established by the 1987 constitution,</div><div>each of which has its own function outside of the government.</div><div><br></div><div>1. COMELEC (Commission on Elections)</div><div>- Has the mandate to conduct elections, certify the results of said elections, and</div><div>proclaim the winners.</div><div><br></div><div>2. Commission on Human Rights</div><div>- Has the mandate to investigate any human rights violation of duty bearers in the</div><div>country.</div><div><br></div><div>3. Civil Service Commissions</div><div>- Has the mandate to oversee the professionalism and integrity of government actions</div><div>and personnel.</div><div><br></div><div>4. Commission on Audit</div><div>- Has the mandate to audit or examine all government accounts and expenditure.</div><div>- Has the power to disallow any irregular or unnecessary expenses by any government</div><div>agencies, as well as to recommend the filing of charges of corruption or plunder.</div>\"},{\"title\":\"Lesson 2\",\"content\":\"This is the learning content for Lesson 2 of SOC 101.\"}]'),
(39, 'STEM A', 'sadw', '30', 'ENGLISH 201', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'approved', NULL, NULL, NULL, NULL, 3, '[{\"title\":\"Lesson 1\",\"content\":\"<h1 style=\\\"text-align: center;\\\"><b>RESEARCH INSTRUMENTS</b></h1><div><b><br></b></div><div><br></div><div><b>● What is a Research Instrument?</b></div><div>- A research instrument is any tool or device used to collect, measure, and analyze</div><div>data to answer research questions or test hypotheses.</div><div><br></div><div><b>● Guidelines:</b></div><div>- Do preliminary research by visiting the library or checking online sources.</div><div>- Ask for advice from experts.</div><div>- Master the guidelines in preparing and administering each type of instrument.</div><div>- Clarify your research questions.</div><div>- Decide on the number and demographics for your respondents.</div><div>- Use the appropriate format of your instrument.</div><div>- Edit your instrument.</div><div>- Pilot your instrument.</div><div><br></div><div><b>● Types of Research Instrument</b></div><div><ol><li>Survey</li><li>Observation</li><li>Experiment</li></ol></div><div><br></div><div><b>● SURVEY</b></div><div>- Contained prepared questions that are used to measure attitudes, perceptions, and</div><div>opinions.</div><div><br></div><div><b>(3) types of question to use when conducting a survey:</b></div><div>a. Recall: it asks for specific information.</div><div>b. Recognition: it asks for a response to a specific question: multiple choice,</div><div>dichotomous (Yes/No), and rating scale.</div><div>c. Open-ended: it elicits brief explanation or impressions from your respondents (to</div><div>give answer from the question)</div><div><br></div><div><b>Forms of Survey:</b></div><div><ol><li>Interview</li></ol></div><div>- An instrument that enables the researcher to collect qualitative data.</div><div>An Interview consists of different stages:</div><div><br></div><div>Stage 1: Pre-interview - Preparation or preparing on what to ask for your interviewee.</div><div>Stage 2: Warm-up - Introducing yourself, explaining the purpose, assuring confidentiality.</div><div>Making the interviewee feel comfortable and assured enough.</div><div>Stage 3: Main Interview - Asks the main question directly related to the research question.</div><div>Stage 4: Closing - It asks questions meant to wind down the interview.</div><div><b><br></b></div><div><b>2. Questionnaire</b></div><div>- Contains written questions that ask for specific information. Responses are</div><div>dichotomous (Yes/No).</div><div><br></div><div><b>Parts of Questionnaire:</b></div><div><ol><li>Personal Informations</li><li>Basic Questions</li><li>Main Questions</li><li>Open-ended Questions</li></ol></div><div><br></div><div><b>● Observations</b></div><div>- Allows the description of a behavior in a naturalistic or laboratory setting, It is useful</div><div>when questions require descriptions of behavior and setting.</div><div><br></div><div><b>&gt; Participants and Non-Participants Observation:</b></div><div>- Non-Participant: allows the researcher to observe the subjects without interacting</div><div>with them.</div><div><br></div><div><b>Example:</b> A teacher being observed by someone at the back of the classroom</div><div>without knowing that it also observes behavior of the students, not just the teacher)</div><div><br></div><div>- Participants: allows the researcher to interact actively with the subjects.</div><div><br></div><div><b>&gt; Structured and Unstructured Observation:</b></div><div>- Structure: Occurs when the researcher has a list of behavior that he/she wants to</div><div>observe.</div><div><br></div><div>- Unstructured: The researcher allows behavior to emerge naturally.</div><div><br></div><div><b>&gt; Covert and Overt Observation:</b></div><div>- Overt: Occurs when subjects are not aware that they are being observed.</div><div><br></div><div>- Overt: Subjects are aware that they are being observed.</div><div><br></div><div><b>● Experiment</b></div><div>- Can be performed in a laboratory or natural setting by following these steps:</div><div>Stage 1: Make Observation</div><div>Stage 2: Develop the experiment</div><div>Stage 3: Design the experiment</div><div>Stage 4: Conduct the experiment</div><div>Stage 5: Replicate the experiment</div><div>Stage 6: Analyze the result</div><div>Stage 7: Whether to accept or reject the hypothesis</div><div><br></div><div><b>● Guidelines:</b></div><div>- Coordinate with the Lab technician.</div><div>- Make yourself present and accessible during the experiment.</div><div>- Maintain a relaxed and calm atmosphere.</div><div>- Informed consent should be given to participants.</div><div>- Ensure safety.</div><div>- Ensure anonymity.</div><div>- Ensure confidentiality of the data.</div><div><br></div><div><b>● In Summary:</b></div><div>- Use Survey when you need opinions or self-reported data.</div><div>- Use Observation when you study behaviors.</div><div>- Use Experiment for cause and effect.</div>\"},{\"title\":\"Lesson 2\",\"content\":\"This is the learning content for Lesson 2 of ENGLISH 201.\"},{\"title\":\"Lesson 3\",\"content\":\"This is the learning content for Lesson 3 of ENGLISH 201.\"}]'),
(40, 'STEM B', 'sadwasd', '30', 'ENGLISH 201', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'approved', NULL, NULL, NULL, NULL, 1, '[{\"title\":\"Lesson 1\",\"content\":\"<h1 style=\\\"text-align: center;\\\"><b>RESEARCH INSTRUMENTS</b></h1><div><b><br></b></div><div><br></div><div style=\\\"text-align: left;\\\"><b>● What is a Research Instrument?</b></div><div>- A research instrument is any tool or device used to collect, measure, and analyze</div><div>data to answer research questions or test hypotheses.</div><div><br></div><div><b>● Guidelines:</b></div><div>- Do preliminary research by visiting the library or checking online sources.<br></div><div>- Ask for advice from experts.</div><div>- Master the guidelines in preparing and administering each type of instrument.</div><div>- Clarify your research questions.</div><div>- Decide on the number and demographics for your respondents.</div><div>- Use the appropriate format of your instrument.</div><div>- Edit your instrument.</div><div>- Pilot your instrument.</div><div><br></div><div><b>● Types of Research Instrument</b></div><div>1. Survey</div><div>2. Observation</div><div>3. Experiment</div><div><br></div><div><b>● SURVEY</b></div><div>- Contained prepared questions that are used to measure attitudes, perceptions, and</div><div>opinions.</div><div><br></div><div><b>(3) types of question to use when conducting a survey:<br><br></b></div><div>a. Recall: it asks for specific information.</div><div>b. Recognition: it asks for a response to a specific question: multiple choice,</div><div>dichotomous (Yes/No), and rating scale.</div><div>c. Open-ended: it elicits brief explanation or impressions from your respondents (to</div><div>give answer from the question)</div><div><b><br></b></div><div><b>Forms of Survey:</b></div><div><b>1. Interview</b></div><div>- An instrument that enables the researcher to collect qualitative data.</div><div>An Interview consists of different stages:</div><div><br></div><div>Stage 1: Pre-interview - Preparation or preparing on what to ask for your interviewee.</div><div><br></div><div>Stage 2: Warm-up - Introducing yourself, explaining the purpose, assuring confidentiality.</div><div>Making the interviewee feel comfortable and assured enough.</div><div><br></div><div>Stage 3: Main Interview - Asks the main question directly related to the research question.</div><div><br></div><div>Stage 4: Closing - It asks questions meant to wind down the interview.</div><div><b><br></b></div><div><b>2. Questionnaire</b></div><div>- Contains written questions that ask for specific information. Responses are</div><div>dichotomous (Yes/No).</div><div><br></div><div><b>Parts of Questionnaire:</b></div><div><ol><li>&nbsp;Personal Informations</li><li>Basic Questions</li><li>Main Questions</li><li>Open-ended Questions</li></ol></div><div><br></div><div><b>● Observations</b></div><div>- Allows the description of a behavior in a naturalistic or laboratory setting, It is useful</div><div>when questions require descriptions of behavior and setting.</div><div><br></div><div><b>&gt; Participants and Non-Participants Observation:</b></div><div>- Non-Participant: allows the researcher to observe the subjects without interacting</div><div>with them.</div><div><br></div><div><b>Example: </b>A teacher being observed by someone at the back of the classroom</div><div>without knowing that it also observes behavior of the students, not just the teacher)</div><div><br></div><div>- Participants: allows the researcher to interact actively with the subjects.</div><div><br></div><div><b>&gt; Structured and Unstructured Observation:</b></div><div>- Structure: Occurs when the researcher has a list of behavior that he/she wants to</div><div>observe.</div><div><br></div><div>- Unstructured: The researcher allows behavior to emerge naturally.</div><div><br></div><div><b>&gt; Covert and Overt Observation:</b></div><div>- Overt: Occurs when subjects are not aware that they are being observed.</div><div><br></div><div>- Overt: Subjects are aware that they are being observed.</div><div><br></div><div><b>● Experiment</b></div><div>- Can be performed in a laboratory or natural setting by following these steps:</div><div>Stage 1: Make Observation</div><div>Stage 2: Develop the experiment</div><div>Stage 3: Design the experiment</div><div>Stage 4: Conduct the experiment</div><div>Stage 5: Replicate the experiment</div><div>Stage 6: Analyze the result</div><div>Stage 7: Whether to accept or reject the hypothesis</div><div><br></div><div><b>● Guidelines:</b></div><div>- Coordinate with the Lab technician.</div><div>- Make yourself present and accessible during the experiment.</div><div>- Maintain a relaxed and calm atmosphere.</div><div>- Informed consent should be given to participants.</div><div>- Ensure safety.</div><div>- Ensure anonymity.</div><div>- Ensure confidentiality of the data.</div><div><br></div><div><b>● In Summary:</b></div><div>- Use Survey when you need opinions or self-reported data.</div><div>- Use Observation when you study behaviors.</div><div>- Use Experiment for cause and effect.</div>\"}]'),
(41, 'STEM B', 'sdadwa', '30', 'FILIPINO 201', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'approved', NULL, NULL, NULL, NULL, 3, '[{\"title\":\"Lesson 1\",\"content\":\"This is the learning content for Lesson 1 of FILIPINO 201.\"},{\"title\":\"Lesson 2\",\"content\":\"This is the learning content for Lesson 2 of FILIPINO 201.\"},{\"title\":\"Lesson 3\",\"content\":\"This is the learning content for Lesson 3 of FILIPINO 201.\"}]');

-- --------------------------------------------------------

--
-- Table structure for table `quizzes`
--

CREATE TABLE `quizzes` (
  `id` bigint(20) NOT NULL,
  `description` varchar(3000) DEFAULT NULL,
  `questions_json` longtext DEFAULT NULL,
  `strand` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `lesson` varchar(255) DEFAULT NULL,
  `lesson_number` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `quizzes`
--

INSERT INTO `quizzes` (`id`, `description`, `questions_json`, `strand`, `title`, `lesson`, `lesson_number`) VALUES
(1, 'SFESFSDFESFDS', '[{\"question\":\"20 + 20 =\",\"choices\":[\"20\",\"40\",\"30\",\"10\"],\"correct\":0}]', 'STEM A', 'MATHEMATICS', NULL, NULL),
(2, 'sdawd', '[{\"question\":\"What is the primary purpose of a research instrument?\",\"choices\":[\"To finalize the research budget\",\"To collect, measure, and analyze data to answer research questions\",\"To recruit participants for future projects\",\"To format the final bibliography\"],\"correct\":1},{\"question\":\"Which survey question type asks respondents for a specific response using multiple-choice, dichotomous (Yes/No), or rating scales?\",\"choices\":[\"Recall questions\",\"Recognition questions\",\"Open-ended questions\",\"Naturalistic questions\"],\"correct\":1},{\"question\":\"During which stage of an interview does the researcher introduce themselves, explain the study\'s purpose, and assure confidentiality?\",\"choices\":[\"Pre-interview\",\"Main Interview\",\"Warm-up\",\"Closing\"],\"correct\":2},{\"question\":\"What is a key characteristic of a Questionnaire?\",\"choices\":[\"It contains written questions asking for specific information with dichotomous choices\",\"It requires active manipulation of variables in a laboratory\",\"It relies strictly on naturalistic observation\",\"It is an unstructured oral conversation\"],\"correct\":0},{\"question\":\"A researcher watches classroom interactions from the back of the room without taking part in the activities. What type of observation is this?\",\"choices\":[\"Participant Observation\",\"Non-Participant Observation\",\"Unstructured Observation\",\"Covert Observation\"],\"correct\":1},{\"question\":\"What type of observation occurs when the subjects are fully aware that they are being observed?\",\"choices\":[\"Covert\",\"Overt\",\"Unstructured\",\"Segmentary\"],\"correct\":1},{\"question\":\"In an observation study, when a researcher goes into the setting with a pre-made checklist of specific behaviors to track, they are conducting:\",\"choices\":[\"Unstructured observation\",\"Structured observation\",\"Covert observation\",\"Participant observation\"],\"correct\":1},{\"question\":\"In an experimental procedure, what step immediately follows \\\"Analyze the result\\\"?\",\"choices\":[\"Make Observation\",\"Conduct the experiment\",\"Decide whether to accept or reject the hypothesis\",\"Design the experiment\"],\"correct\":2},{\"question\":\"Which ethical guideline requires researchers to ensure participants voluntarily agree to take part after understanding the study\'s details?\",\"choices\":[\"Informed consent\",\"Eminent domain\",\"Pilot testing\",\"Open-ended sampling\"],\"correct\":0},{\"question\":\"Which research instrument is best suited when your goal is to establish a cause-and-effect relationship?\",\"choices\":[\"Survey\",\"Observation\",\"Questionnaire\",\"Experiment\"],\"correct\":3}]', 'STEM B', 'RESEARCH INSTRUMENTS', 'ENGLISH 201', 'Lesson 1');

-- --------------------------------------------------------

--
-- Table structure for table `strands`
--

CREATE TABLE `strands` (
  `id` bigint(20) NOT NULL,
  `description` varchar(3000) DEFAULT NULL,
  `icon` varchar(100) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `strands`
--

INSERT INTO `strands` (`id`, `description`, `icon`, `name`, `status`) VALUES
(5, 'Science, Technology, Engineering, and Mathematics learning resources for STEM A students.', 'fa-flask', 'STEM A', 'active'),
(6, 'Science, Technology, Engineering, and Mathematics learning resources for STEM B students.', 'fa-flask', 'STEM B', 'active'),
(7, 'Science, Technology, Engineering, and Mathematics learning resources for STEM B students.', 'fa-book-open', 'STEM C', 'inactive');

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `id` bigint(20) NOT NULL,
  `course` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `student_id` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `year_level` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`id`, `course`, `email`, `full_name`, `password`, `role`, `student_id`, `username`, `year_level`) VALUES
(1, 'Administration', 'admin@memorylink.local', 'Administrator', '$2a$10$1PGtR6VXG8/4BPWx8aR80eQoyfBuxuMOe.TWhvjHxtl7itKgvnxdi', 'admin', 'ADMIN-001', 'admin', 'N/A'),
(2, 'BSIT', 'louisevestal@xu.edu.ph', 'louise vestal', '$2a$10$cfYFRmq6JUbFs/tNF4gdQeB2jJyItWo39OVLAWmSqCdUtYJdjKOU2', 'student', '20220074', '@louise', '1st Year'),
(3, 'N/A', 'colet@xu.edu.ph', 'Colet vergara', '$2a$10$OZ1EyQGda9gvz1fQKGhgGORuFMwmyLNx.wS1XHt1AyxaJQm1Bo2YC', 'student', '20220054', 'Colet14', '12-STEM-A');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `flashcards`
--
ALTER TABLE `flashcards`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `lessons`
--
ALTER TABLE `lessons`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `quizzes`
--
ALTER TABLE `quizzes`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `strands`
--
ALTER TABLE `strands`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKhtx2tveevnne9t59jxn9qtg0b` (`name`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKakwqgcdnid3qo41cqpdu4ke01` (`username`),
  ADD UNIQUE KEY `UKe2rndfrsx22acpq2ty1caeuyw` (`email`),
  ADD UNIQUE KEY `UK5mbus2m1tm2acucrp6t627jmx` (`student_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `flashcards`
--
ALTER TABLE `flashcards`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `lessons`
--
ALTER TABLE `lessons`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT for table `quizzes`
--
ALTER TABLE `quizzes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `strands`
--
ALTER TABLE `strands`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `students`
--
ALTER TABLE `students`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
