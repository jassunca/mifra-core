package org.mifra.core.components.stepmaps;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds and represents the general flow of a saga orchestration, defined via the orchestrator.
 */
public class SagaStepMap {

    private final Map<String, StepNode> flowGraph = new HashMap<>();
    private String initialStep;

    public void setInitialStep(String initialStep) {
        this.initialStep = initialStep;
    }

    public String getInitialStep() {
        return initialStep;
    }

    public StepNode configureStep(String label) {
        return flowGraph.computeIfAbsent(label, StepNode::new);
    }

    public StepNode getStep(String label) {
        return flowGraph.get(label);
    }

    /**
     * SagaStepMap inner class defining the possible next steps to a given step
     */
    public static class StepNode {
        private final String stepLabel;
        private final Map<String, String> transitions = new HashMap<>();

        public StepNode(String stepLabel) {
            this.stepLabel = stepLabel;
        }

        public String getStepLabel() {
            return stepLabel;
        }

        /**
         * Used in conjunction with the SagaStepMap's configureStep(String label) method, creates a next step label
         * based on the outcome label that the step associated to the node produces.
         * @param outcome The outcome status that a node's associated step may produce.
         * @param nextStepLabel The label for the next step to execute based on the obtained outcome label.
         * @return This node instance to chain a subsequent step result addition if wanted.
         */
        public StepNode addStepResult(String outcome, String nextStepLabel) {
            this.transitions.put(outcome.toUpperCase(), nextStepLabel);
            return this;
        }

        /**
         * INTERNAL USE METHOD
         * Get the label for the next step to execute based on the obtained outcome label.
         * @param outcome The outcome status label which resulted from the last executed step.
         * @return The label for the next step to execute.
         */
        public String getNextStep(String outcome) {
            return this.transitions.get(outcome.toUpperCase());
        }
    }
}
