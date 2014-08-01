package org.motechproject.vxml.web;

import org.motechproject.vxml.alert.MotechStatusMessage;
import org.motechproject.vxml.config.ConfigHelper;
import org.motechproject.vxml.domain.CallDetailRecord;
import org.motechproject.vxml.domain.Config;
import org.motechproject.vxml.repository.CallDetailRecordDataService;
import org.motechproject.vxml.repository.ConfigDataService;
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
@RequestMapping(value = "/status")
public class StatusController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CallDetailRecordDataService callDetailRecordDataService;
    @Autowired
    private ConfigDataService configDataService;
    @Autowired
    private MotechStatusMessage motechStatusMessage;

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/{configName}")
    public void handle(@PathVariable String configName, @RequestParam Map<String, String> params) {
        logger.debug("configName = {}, params = {}", configName, params);

        Config config = configDataService.findByName(configName);
        if (null == config){
            String msg = String.format("No matching config in the database for: %s", configName);
            logger.error(msg);
            motechStatusMessage.alert(msg);
        }

        CallDetailRecord callDetailRecord = new CallDetailRecord();
        callDetailRecord.config = config.name;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            ConfigHelper.setCallDetail(config, entry.getKey(), entry.getValue(), callDetailRecord);
        }

        logger.debug("Saving CallDetailRecord {}", callDetailRecord);
        callDetailRecordDataService.create(callDetailRecord);
    }
}
