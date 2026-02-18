package com.gpsolutions.propertyview.service.histogram;

import java.util.Map;

public interface HistogramStrategy {

    boolean supports(String param);

    Map<String, Long> compute();
}

