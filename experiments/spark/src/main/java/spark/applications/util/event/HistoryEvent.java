/**
 * Copyright 2015 Miyuru Dayarathna
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spark.applications.util.event;

/**
 * @author miyuru
 */
public class HistoryEvent {
    //carid, d, x, daily_exp
    public int carid;
    public int d;
    public int x;
    public int daily_exp;

    public HistoryEvent(int carid, int d, int x, int daily_exp) {
        super();
        this.carid = carid;
        this.d = d;
        this.x = x;
        this.daily_exp = daily_exp;
    }


}
