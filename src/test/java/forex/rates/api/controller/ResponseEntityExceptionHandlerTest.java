package forex.rates.api.controller;

import forex.rates.api.service.DateTimeProviderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResponseEntityExceptionHandlerTest {

    private @Mock DateTimeProviderService dateTimeProviderService;

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
	MockitoAnnotations.initMocks(this);
	DummyController dummyController = new DummyController();
	ResponseEntityExceptionHandler exceptionHandler = new ResponseEntityExceptionHandler(dateTimeProviderService);
	mockMvc = MockMvcBuilders.standaloneSetup(dummyController).setControllerAdvice(exceptionHandler).build();
    }

    @Test
    public void shouldHandleIllegalArgumentExceptionAndReturnCustomJsonResponse() throws Exception {
	when(dateTimeProviderService.getCurrentTimestamp()).thenReturn(1234L);

	mockMvc.perform(get("/throwIllegalArgumentException")
			.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest())
		.andExpect(content().json("{'error':true}"))
		.andExpect(content().json("{'timestamp':1234}"))
		.andExpect(content().json("{'httpStatus':400}"))
		.andExpect(content().json("{'message':'Bad Request'}"))
		.andExpect(content().json("{'description':'Some description'}"));
    }

}
