/*
 * <<
 *  Davinci
 *  ==
 *  Copyright (C) 2016 - 2020 EDP
 *  ==
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  >>
 *
 */

package edp.davinci.server.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edp.davinci.commons.util.JSONUtils;
import edp.davinci.data.pojo.SourceConfig;
import edp.davinci.data.source.JdbcDataSource;
import edp.davinci.data.util.JdbcSourceUtils;
import edp.davinci.server.service.RedisMessageHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SourceMessageHandler implements RedisMessageHandler {

    @Autowired
	JdbcDataSource jdbcDataSource;

	@Override
    public void handle(Object message, String flag) {
        
        if (!(message instanceof String)) {
            return;
        }

        if (JdbcDataSource.getReleaseSet().contains(flag)) {
            JdbcDataSource.getReleaseSet().remove(flag);
            return;
        }
        
        SourceConfig config = JSONUtils.toObject((String)message, SourceConfig.class);
        JdbcSourceUtils utils = new JdbcSourceUtils(jdbcDataSource);
        utils.releaseDataSource(config);
        log.info("SourceMessageHandler release source whit message:{}, id:{}", message, flag);
    }
}