package com.myapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.myapp.service.StudentResource;
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@RunWith(SpringRunner.class)
	@WebMvcTest(value = StudentController.class, secure = false)
	public class StudentControllerTest {

		@Autowired
		private MockMvc mockMvc;

		@MockBean
		private StudentService studentService;

		Course mockCourse = new Course("Course1", "Spring", "10 Steps", Arrays
				.asList("Learn Maven", "Import Project", "First Example",
						"Second Example"));

		String exampleCourseJson = "{\"name\":\"Spring\",\"description\":\"10 Steps\",\"steps\":[\"Learn Maven\",\"Import Project\",\"First Example\",\"Second Example\"]}";

		@Test
		public void retrieveDetailsForCourse() throws Exception {

			Mockito.when(
					studentService.retrieveCourse(Mockito.anyString(), Mockito
							.anyString())).thenReturn(mockCourse);

			RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
					"/students/Student1/courses/Course1").accept(
					MediaType.APPLICATION_JSON);

			MvcResult result = mockMvc.perform(requestBuilder).andReturn();

			System.out.println(result.getResponse());
			String expected = "{id:Course1,name:Spring,description:10 Steps}";

			//{"id":"Course1","name":"Spring","description":"10 Steps, 25 Examples and 10K Students","steps":["Learn Maven","Import Project","First Example","Second Example"]}

			JSONAssert.assertEquals(expected, result.getResponse()
					.getContentAsString(), false);
		}

		@Test
		public void createStudentCourse() throws Exception {
			Course mockCourse = new Course("1", "Smallest Number", "1", Arrays
					.asList("1", "2", "3", "4"));

			//studentService.addCourse to respond back with mockCourse
			Mockito.when(
					studentService.addCourse(Mockito.anyString(), Mockito
							.any(Course.class))).thenReturn(mockCourse);

			//Send course as body to /students/Student1/courses
			RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
					"/students/Student1/courses")
					.accept(MediaType.APPLICATION_JSON).content(exampleCourseJson)
					.contentType(MediaType.APPLICATION_JSON);

			MvcResult result = mockMvc.perform(requestBuilder).andReturn();

			MockHttpServletResponse response = result.getResponse();

			assertEquals(HttpStatus.CREATED.value(), response.getStatus());

			assertEquals("http://localhost/students/Student1/courses/1", response
					.getHeader(HttpHeaders.LOCATION));

		}

	}

	@Test
	public void contextLoads() {
	}

}
