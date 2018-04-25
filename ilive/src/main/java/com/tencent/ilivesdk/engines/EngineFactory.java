package com.tencent.ilivesdk.engines;

import com.tencent.ilivesdk.engines.communication.CommunicationEngine;
import com.tencent.ilivesdk.engines.communication.TIMCommunicationEngine;

/**
 *
 */

public class EngineFactory {

    private EngineFactory() {}

    private static CommunicationEngine communicationEngine = null;

    public static synchronized CommunicationEngine provideCommunicationEngine() {
        if (communicationEngine == null) {
            communicationEngine = new TIMCommunicationEngine();
        }
        return communicationEngine;
    }
}
