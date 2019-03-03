/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.jene.cognitive.serde;

import com.jene.cognitive.model.Transaction;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.types.Row;
import org.apache.flink.util.Preconditions;

import java.io.IOException;

/**
 * Deserialization schema from JSON to {@link Row}.
 *
 * @uthor Jorge Nieves
 * <p>Deserializes the <code>byte[]</code> messages as a JSON object and reads
 * the specified fields.
 *
 * <p>Failure during deserialization are forwarded as wrapped IOExceptions.
 */
public class JsonSerdeTransaction implements DeserializationSchema<Transaction> {


    /**
     * Type information describing the result type.
     */
    private final TypeInformation<Transaction> typeInfo;


    /**
     * Object mapper for parsing the JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Flag indicating whether to fail on a missing field.
     */
    private boolean failOnMissingField;

    public JsonSerdeTransaction(TypeInformation<Transaction> typeInfo) {
        Preconditions.checkNotNull(typeInfo, "Type information");
        this.typeInfo = typeInfo;

    }

    @Override
    public Transaction deserialize(byte[] message) throws IOException {
        try {

            Transaction t = objectMapper.readValue(message, Transaction.class);
            return t;

        } catch (Throwable t) {

            throw new IOException("Failed to deserialize JSON object.", t);
        }
    }

    @Override
    public TypeInformation<Transaction> getProducedType() {
        return typeInfo;
    }


    @Override
    public boolean isEndOfStream(Transaction nextElement) {
        return false;
    }

    /**
     * Configures the failure behaviour if a JSON field is missing.
     *
     * <p>By default, a missing field is ignored and the field is set to null.
     *
     * @param failOnMissingField Flag indicating whether to fail or not on a missing field.
     */
    public void setFailOnMissingField(boolean failOnMissingField) {
        this.failOnMissingField = failOnMissingField;
    }

}
