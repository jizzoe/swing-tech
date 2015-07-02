/*
 * SwingTech Software - http://cooksarm.sourceforge.net/
 *
 * Copyright (C) 2011 Joe Rice
 * All rights reserved.
 * 
 * SwingTech Cooks Arm is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * SwingTech Cooks Arm is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwingTech Cooks Arm; If not, see <http://www.gnu.org/licenses/>. 
 * 
 */
package com.swingtech.apps.filemgmt.util;

import java.util.Date;

/**
 * Class provides stopwatch like functionality to time the execution of java calls.
 * 
 * Usage: 
 * Create a new Timer instance.  Call timer.startTiming() before executing the 
 * block of code you want to time.  Call timer.stopTiming() after the block of code.  
 * 
 * After the timer has stopped, there are various getter methods you can call to get additional info including duration. 
 * 
 * Created:
 * 
 * @author jorice
 */
public class Timer {
    private long _startTimeMilis = 0;
    private long _endTimeMilis = 0;
    private long _startFreeMemory = 0;
    private long _endFreeMemory = 0;
    private long _startTotalMemory = 0;
    private long _endTotalMemory = 0;

    /**
     * Create new instance of Timer
     * 
     * @param
     * @exception
     * @since
     */
    public Timer() {

    }

    /**
     * Start the timing
     * 
     */
    public void startTiming() {
        Runtime runtime = Runtime.getRuntime();
        _startTimeMilis = System.currentTimeMillis();
        _startFreeMemory = runtime.freeMemory();
        _startTotalMemory = runtime.totalMemory();
    }

    /**
     * Stop the timing
     * 
     */
    public void stopTiming() {
        Runtime runtime = Runtime.getRuntime();
        _endTimeMilis = System.currentTimeMillis();
        _endFreeMemory = runtime.freeMemory();
        _endTotalMemory = runtime.totalMemory();
    }

    /**
     * Returns the time the timer was started...in milis
     *
     * @return
     */
    public long getStartTimeMillis() {
        return _startTimeMilis;
    }

    /**
     * Returns the time the timer was stopped...in milis
     *
     * @return
     */
    public long getEndTimeMillis() {
        return _endTimeMilis;
    }

    /**
     * Returns the date the timer was started
     *
     * @return
     */
    public Date getStartTimeDate() {
        return new Date(_startTimeMilis);
    }

    /**
     * Returns the date the timer was stopped
     *
     * @return
     */
    public Date getEndTimeDate() {
        return new Date(_endTimeMilis);
    }

    /**
     * @deprecated - Now use getDuration() instead.
     */
    public long getTime() {
        return getDuration();
    }

    /**
     * returns the duration between startTiming() and stopTiming().
     *
     * @return miliseconds between startTiming() and stopTiming()
     */
    public long getDuration() {
        if (_endTimeMilis == 0 || _startTimeMilis == 0)
            return 0;

        return _endTimeMilis - _startTimeMilis;
    }

    /**
     * @deprecated - Now use getDurationString() instead.
     */
    public String getTimeString() {
        return getDurationString();
    }

    public String getDurationString() {
        double mili = getDuration();
        double time = 0;

        if (mili < 1000) {
            time = mili;
            time = Utility.decimalRound(time, 2);
            return Double.toString(time) + " mili seconds";
        }

        if (mili < 60000) {
            time = mili / 1000;
            time = Utility.decimalRound(time, 2);
            return Double.toString(time) + " seconds";
        }

        if (mili < 3600000) {
            time = mili / 60000;
            time = Utility.decimalRound(time, 2);
            return Double.toString(time) + " minutes";
        }

        if (mili < 86400000) {
            time = mili / 3600000;
            time = Utility.decimalRound(time, 2);
            return Double.toString(time) + " hours";
        }

        if (mili >= 86400000) {
            time = mili / 86400000;
            time = Utility.decimalRound(time, 2);
            return Double.toString(time) + " days";
        }

        time = Utility.decimalRound(time, 2);
        return Double.toString(time) + " days";
    }

    public String getMemoryUsed() {
        float memUsedBytes = _startFreeMemory - _endFreeMemory;
        float memUsedKBytes = memUsedBytes / 1024;

        return Float.toString(memUsedKBytes) + " KB";
    }

    public long getStartFreeMemoryBytes() {
        return _startFreeMemory;
    }

    public long getEndFreeMemoryBytes() {
        return _endFreeMemory;
    }

    public long getStartTotalMemoryBytes() {
        return _startTotalMemory;
    }

    public long getEndTotalMemoryBytes() {
        return _endTotalMemory;
    }

    public String getStartFreeMemory() {
        return Float.toString(((float) _startFreeMemory) / 1024) + " KB";
    }

    public String getEndFreeMemory() {
        return Float.toString(((float) _endFreeMemory) / 1024) + " KB";
    }

    public String getStartTotalMemory() {
        return Float.toString(((float) _startTotalMemory) / 1024) + " KB";
    }

    public String getEndTotalMemory() {
        return Float.toString(((float) _endTotalMemory) / 1024) + " KB";
    }
}