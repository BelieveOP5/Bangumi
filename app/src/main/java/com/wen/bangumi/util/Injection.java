/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.wen.bangumi.util;

import com.wen.bangumi.data.CalendarRepository;
import com.wen.bangumi.data.LoginRepository;
import com.wen.bangumi.util.scheduler.BaseSchedulerProvider;
import com.wen.bangumi.util.scheduler.SchedulerProvider;

/**
 * Enables injection of production implementations for
 * {@link CalendarRepository} at compile time.
 */
public class Injection {

    public static LoginRepository provideLoginRepository() {
        return LoginRepository.getInstance();
    }

    public static CalendarRepository provideCalendarRepository() {
        return CalendarRepository.getInstance();
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

}
