package com.jene.cognitive.model.cep;

import org.apache.flink.cep.pattern.Pattern;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @uthor Jorge Nieves
 */
public interface IWarningPattern<TEventType, TWarningType extends IWarning> extends Serializable {

    /**
     * Implements the mapping between the pattern matching result and the warning.
     *
     * @param pattern Pattern, which has been matched by Apache Flink.
     * @return The warning created from the given match result.
     */
    TWarningType create(Map<String, List<TEventType>> pattern);

    /**
     * Implementes the Apache Flink CEP Event Pattern which triggers a warning.
     *
     * @return The Apache Flink CEP Pattern definition.
     */
    Pattern<TEventType, ?> getEventPattern();

    /**
     * Returns the Warning Class for simplifying reflection.
     *
     * @return Class Type of the Warning.
     */
    Class<TWarningType> getWarningTargetType();

}