package com.creator.common.bean;

import com.google.gson.Gson;

import java.util.List;

public class PingResult {
    private boolean result;
    private String code;
    private String message;
    private String pingHost;
    private List<PingResultDetail> pingResultDetail;
    private String IPversion;

    public static PingResult toClass(String str) {
        return new Gson().fromJson(str, PingResult.class);
    }

    public static class PingResultDetail {
        private boolean result;
        private String code;
        private String message;
        private String pingServerArea;
        private PingResultData pingResult;
        private PingIPLocation pingIPLocation;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPingServerArea() {
            return pingServerArea;
        }

        public void setPingServerArea(String pingServerArea) {
            this.pingServerArea = pingServerArea;
        }

        public PingResultData getPingResult() {
            return pingResult;
        }

        public void setPingResult(PingResultData pingResult) {
            this.pingResult = pingResult;
        }

        public PingIPLocation getPingIPLocation() {
            return pingIPLocation;
        }

        public void setPingIPLocation(PingIPLocation pingIPLocation) {
            this.pingIPLocation = pingIPLocation;
        }
// 添加构造方法、getter 和 setter 方法

        public static class PingResultData {
            private String pingHost;
            private String pingIP;
            private int sendPackets;
            private int receivedPackets;
            private int lossPacket;
            private int totalElapsedTime;
            private double rttMinTime;
            private double rttAvgTime;
            private double rttMaxTime;
            private double rttMdevTime;
            private String pingStatisticsDetail;

            public String getPingHost() {
                return pingHost;
            }

            public void setPingHost(String pingHost) {
                this.pingHost = pingHost;
            }

            public String getPingIP() {
                return pingIP;
            }

            public void setPingIP(String pingIP) {
                this.pingIP = pingIP;
            }

            public int getSendPackets() {
                return sendPackets;
            }

            public void setSendPackets(int sendPackets) {
                this.sendPackets = sendPackets;
            }

            public int getReceivedPackets() {
                return receivedPackets;
            }

            public void setReceivedPackets(int receivedPackets) {
                this.receivedPackets = receivedPackets;
            }

            public int getLossPacket() {
                return lossPacket;
            }

            public void setLossPacket(int lossPacket) {
                this.lossPacket = lossPacket;
            }

            public int getTotalElapsedTime() {
                return totalElapsedTime;
            }

            public void setTotalElapsedTime(int totalElapsedTime) {
                this.totalElapsedTime = totalElapsedTime;
            }

            public double getRttMinTime() {
                return rttMinTime;
            }

            public void setRttMinTime(double rttMinTime) {
                this.rttMinTime = rttMinTime;
            }

            public double getRttAvgTime() {
                return rttAvgTime;
            }

            public void setRttAvgTime(double rttAvgTime) {
                this.rttAvgTime = rttAvgTime;
            }

            public double getRttMaxTime() {
                return rttMaxTime;
            }

            public void setRttMaxTime(double rttMaxTime) {
                this.rttMaxTime = rttMaxTime;
            }

            public double getRttMdevTime() {
                return rttMdevTime;
            }

            public void setRttMdevTime(double rttMdevTime) {
                this.rttMdevTime = rttMdevTime;
            }

            public String getPingStatisticsDetail() {
                return pingStatisticsDetail;
            }

            public void setPingStatisticsDetail(String pingStatisticsDetail) {
                this.pingStatisticsDetail = pingStatisticsDetail;
            }
// 添加构造方法、getter 和 setter 方法
        }

        public static class PingIPLocation {
            private String Address;
            private String ISP;

            public String getAddress() {
                return Address;
            }

            public void setAddress(String address) {
                Address = address;
            }

            public String getISP() {
                return ISP;
            }

            public void setISP(String ISP) {
                this.ISP = ISP;
            }
// 添加构造方法、getter 和 setter 方法
        }
    }

    // 添加构造方法、getter 和 setter 方法

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPingHost() {
        return pingHost;
    }

    public void setPingHost(String pingHost) {
        this.pingHost = pingHost;
    }

    public List<PingResultDetail> getPingResultDetail() {
        return pingResultDetail;
    }

    public void setPingResultDetail(List<PingResultDetail> pingResultDetail) {
        this.pingResultDetail = pingResultDetail;
    }

    public String getIPversion() {
        return IPversion;
    }

    public void setIPversion(String IPversion) {
        this.IPversion = IPversion;
    }
}

