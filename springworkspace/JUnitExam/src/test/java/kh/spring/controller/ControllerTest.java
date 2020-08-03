package kh.spring.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  
@ContextConfiguration(locations= {
      "file:src/main/webapp/WEB-INF/spring/root-context.xml",
      "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})

public class ControllerTest {
   
   private static final Logger Logger = LoggerFactory.getLogger(""); 
   
   @Autowired
   private WebApplicationContext was;
   private MockMvc mockMvc;
   
   @Before
   public void setup() {   
      this.mockMvc = MockMvcBuilders.webAppContextSetup(this.was).build();
      //System.out.println("MockMvc 인스턴스 생성 완료!!");
      Logger.error("MockMvc 인스턴스 생성 완료!!");
   }
   
   @Test
   public void insertTest() {
      try{
         mockMvc.perform(
            MockMvcRequestBuilders.post("/insert").
            param("id", "ABC").
            param("pw", "PWD")
         ).andDo(MockMvcResultHandlers.print())
         .andExpect(MockMvcResultMatchers.status().isOk());
         
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   @Test
   public void selectTest() {
      try{
         mockMvc.perform( 
            MockMvcRequestBuilders.post("/select").
            param("seq", "100")
         ).andDo(MockMvcResultHandlers.print())
         .andExpect(MockMvcResultMatchers.status().is4xxClientError());
         
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
}