/*
 * Copyright (C) 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.stream.binder.rocketmq;

import com.alibaba.cloud.stream.binder.rocketmq.config.RocketMQBinderAutoConfiguration;
import com.alibaba.cloud.stream.binder.rocketmq.properties.RocketMQBinderConfigurationProperties;
import com.alibaba.cloud.stream.binder.rocketmq.properties.RocketMQExtendedBindingProperties;

import org.junit.Before;
import org.junit.Test;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 */
public class RocketMQAutoConfigurationTests {

	private ConfigurableApplicationContext context;

	@Before
	public void setUp() throws Exception {
		this.context = new SpringApplicationBuilder(RocketMQBinderAutoConfiguration.class)
				.web(false)
				.run("--spring.cloud.stream.rocketmq.binder.namesrv-addr=127.0.0.1:9876",
						"--spring.cloud.stream.bindings.output.destination=TopicOrderTest",
						"--spring.cloud.stream.bindings.output.content-type=application/json",
						"--spring.cloud.stream.bindings.input1.destination=TopicOrderTest",
						"--spring.cloud.stream.bindings.input1.content-type=application/json",
						"--spring.cloud.stream.bindings.input1.group=test-group1",
						"--spring.cloud.stream.rocketmq.bindings.input1.consumer.orderly=true",
						"--spring.cloud.stream.bindings.input1.consumer.maxAttempts=1",
						"--spring.cloud.stream.bindings.input2.destination=TopicOrderTest",
						"--spring.cloud.stream.bindings.input2.content-type=application/json",
						"--spring.cloud.stream.bindings.input2.group=test-group2",
						"--spring.cloud.stream.rocketmq.bindings.input2.consumer.orderly=false",
						"--spring.cloud.stream.rocketmq.bindings.input2.consumer.tags=tag1");
	}

	@Test
	public void testProperties() {
		RocketMQBinderConfigurationProperties binderConfigurationProperties = context
				.getBean(RocketMQBinderConfigurationProperties.class);
		assertThat(binderConfigurationProperties.getNameServer())
				.isEqualTo("127.0.0.1:9876");
		RocketMQExtendedBindingProperties bindingProperties = context
				.getBean(RocketMQExtendedBindingProperties.class);
		assertThat(bindingProperties.getExtendedConsumerProperties("input2").getTags())
				.isEqualTo("tag1");
		assertThat(bindingProperties.getExtendedConsumerProperties("input2").getOrderly())
				.isFalse();
		assertThat(bindingProperties.getExtendedConsumerProperties("input1").getOrderly())
				.isTrue();
	}

}