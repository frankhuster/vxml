package org.motechproject.vxml.web;

import org.motechproject.vxml.service.OutboundCallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * todo
 */
@Controller
@RequestMapping(value = "/call")
public class CallController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private OutboundCallService outboundCallService;

    @Autowired
    public CallController(OutboundCallService outboundCallService) {
        this.outboundCallService = outboundCallService;
    }

    /**
     * Initiates an outbound call
     *
     * @param configName
     * @param params
     */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/{configName}")
    public void handle(@PathVariable String configName, @RequestParam Map<String, String> params,
                       @RequestHeader Map<String, String> headers) {

        //todo *********************************************************************************************************
        //todo
        //todo: remember to change the supportedThemes entry to USERNAME_PASSWORD in securityRules.json after debugging
        //todo
        //todo *********************************************************************************************************

        logger.debug(String.format("handle(configName = %s, params = %s, headers = %s)", configName, params, headers));

        outboundCallService.initiateCall(configName, params);
    }
}
