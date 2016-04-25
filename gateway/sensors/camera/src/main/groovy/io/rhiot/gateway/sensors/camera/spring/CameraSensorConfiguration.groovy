/**
 * Licensed to the Rhiot under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.rhiot.gateway.sensors.camera.spring

import io.rhiot.cloudplatform.connector.IoTConnector
import io.rhiot.gateway.sensors.camera.CameraSensor
import io.rhiot.gateway.sensors.camera.Raspistill
import io.rhiot.utils.process.DefaultProcessManager
import io.rhiot.utils.process.ProcessManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(name = 'sensor.camera.enabled', havingValue = 'true')
class CameraSensorConfiguration {

    @Bean
    CameraSensor cameraSensor(IoTConnector connector, Raspistill raspistill,
                              @Value('${sensor.camera.workdir:/tmp/camera}') File workdir,
                              @Value('${deviceId}') String deviceId, @Value('${sensor.camera.sendToCloud:true}') boolean sendToCloud) {
        new CameraSensor(connector, raspistill, workdir, deviceId, sendToCloud)
    }

    @Bean
    Raspistill raspistill(ProcessManager processManager, @Value('${sensor.camera.workdir:/tmp/camera}') File workdir) {
        new Raspistill(processManager, workdir)
    }

    @Bean(destroyMethod = 'close')
    @ConditionalOnMissingBean
    ProcessManager processManager() {
        new DefaultProcessManager()
    }

}