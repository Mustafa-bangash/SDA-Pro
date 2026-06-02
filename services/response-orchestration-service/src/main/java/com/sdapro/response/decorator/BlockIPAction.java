package com.sdapro.response.decorator;

public class BlockIPAction implements ResponseAction {
    @Override
    public void execute() {
        System.out.println(">>> ACTION: Blocking the malicious IP Address...");
    }
}
