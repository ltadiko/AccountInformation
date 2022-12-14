package nl.rabo.accountinformation.aop;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AspectLoggingTest {

    private MockHttpServletRequest request = new MockHttpServletRequest();
    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;
    @Mock
    private Signature signature;
    @Mock
    private Object object;
    @Mock
    private Exception exception;

    private Logger logger;
    private ListAppender<ILoggingEvent> listAppender;
    private AspectLogging underTest;

    @BeforeEach
    void setup() {

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        logger = loggerContext.getLogger(AspectLogging.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        underTest = new AspectLogging();
    }


    @Test
    @DisplayName("SHOULD log enter and exit logs")
    void testLogControllers() throws Throwable {
        // given
        request.setRequestURI("/users/12341234");
        request.setMethod("GET");
        Map<String, String> pathVariables = new HashMap<>();
        request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, pathVariables);
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        logger.setLevel(Level.DEBUG);

        // when
        underTest.logControllers(proceedingJoinPoint);

        //then
        assertEquals(2, listAppender.list.size());
        verify(proceedingJoinPoint).proceed();
        verify(proceedingJoinPoint, times(4)).getSignature();
        verify(signature, times(2)).getDeclaringTypeName();
        verify(signature, times(2)).getName();
        verifyNoMoreInteractions(signature, proceedingJoinPoint);
    }


    @Test
    @DisplayName("Should log game nl.intergamma.services method enter and exit logs when log level is debug and no exception thrown by the method")
    void test_log_backend_service() throws Throwable {
        // given
        request.setRequestURI("/users");
        request.setMethod("GET");
        request.setAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE, "/stores");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.proceed()).willReturn(object);
        logger.setLevel(Level.DEBUG);

        // when
        Object resultObject = underTest.logService(proceedingJoinPoint);

        // then
        assertEquals(object, resultObject);
        assertNotNull(listAppender.list.get(0));
        assertNotNull(listAppender.list.get(1));
        verify(signature, times(2)).getDeclaringTypeName();
        verify(signature, times(2)).getName();
        verify(proceedingJoinPoint).proceed();
        verifyNoMoreInteractions(signature, proceedingJoinPoint);
    }

    @Test
    @DisplayName("Should not log backend nl.intergamma.services method enter and exit logs when log level is info and no exception thrown by the method")
    void test_log_backend_service_log_level_info() throws Throwable {
        // given
        request.setRequestURI("/users");
        request.setMethod("GET");
        request.setAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE, "/stores/");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.proceed()).willReturn(object);
        logger.setLevel(Level.INFO);

        // when
        Object resultObject = underTest.logService(proceedingJoinPoint);

        // then
        assertEquals(object, resultObject);
        assertEquals(0, listAppender.list.size());
        verify(signature, times(2)).getDeclaringTypeName();
        verify(signature, times(2)).getName();
        verify(proceedingJoinPoint).proceed();
        verifyNoMoreInteractions(signature, proceedingJoinPoint);
    }

    @Test
    @DisplayName("Should not log backend nl.intergamma.services method enter and exit/exception logs when log level is debug and exception thrown by the method")
    void test_log_backend_service_log_level_info_withException() throws Throwable {
        // given
        request.setRequestURI("/users");
        request.setMethod("GET");
        request.setAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE, "/stores/");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(proceedingJoinPoint.proceed()).willThrow(exception);
        logger.setLevel(Level.INFO);

        // when then
        assertThrows(exception.getClass(), () -> underTest.logService(proceedingJoinPoint));
        assertEquals(0, listAppender.list.size());
        verify(signature, times(1)).getDeclaringTypeName();
        verify(signature, times(1)).getName();
        verify(proceedingJoinPoint).proceed();
        verifyNoMoreInteractions(signature, proceedingJoinPoint, exception);
    }
}
