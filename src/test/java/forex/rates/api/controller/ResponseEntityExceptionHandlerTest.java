package forex.rates.api.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResponseEntityExceptionHandlerTest {

    private MockMvc mockMvc;

    @RestController
    private class DummyController {
	@GetMapping("throwIllegalArgumentException")
	void throwIllegalArgumentException() {
	    throw new IllegalArgumentException("Some description");
	}
    }

    @Before
    public void before() {
	mockMvc = MockMvcBuilders.standaloneSetup(new DummyController())
		.setControllerAdvice(new ResponseEntityExceptionHandler())
		.build();
    }

    @Test
    public void shouldHandleIllegalArgumentExceptionAndReturnCustomJsonResponse() throws Exception {
	mockMvc.perform(get("/throwIllegalArgumentException")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest())
		.andExpect(content().json("{'error':true}"))
		.andExpect(content().json("{'httpStatus':400}"))
		.andExpect(content().json("{'message':'Bad Request'}"))
		.andExpect(content().json("{'description':'Some description'}"));
    }

}
