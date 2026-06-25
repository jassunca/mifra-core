package org.mifra;

import org.mifra.core.MifraEngine;
import org.mifra.testobjects.*;

public class Main {
    static void main() throws Exception {

        MifraEngine engine = new MifraEngine();

        // Wire the application team's custom orchestrator into our network entry point
        TestingOrchestrator orch = new TestingOrchestrator();
        engine.registerOrchestrator("/api/first", TestRequestBody.class, TestReplyBody.class, orch);
        TestingParticipantFirst participantService = new TestingParticipantFirst();
        engine.registerParticipantStep("TheFirstStep",participantService::handleOncomingMessage);
        TestingParticipantSecond secondParticipantService = new TestingParticipantSecond();
        engine.registerParticipantStep("TheSecondStep",secondParticipantService::handleOncomingMessage);
        System.out.println("Launching Microservice...");
        engine.startAndJoin();

    }
}
