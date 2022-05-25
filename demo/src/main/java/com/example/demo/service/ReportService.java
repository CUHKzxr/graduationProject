package com.example.demo.service;

public interface ReportService {
    /**
     * 获取数据库中的RSI与RSS报告
     *
     * @param timeString 包含具体月份
     * @return 报告json字符串
     */
    public String getExistedReport(String timeString);

    public String getExistedReport(String startTime, String endTime);

    /**
     * 临时生成报告
     *
     * @return 报告json字符串
     */
    public String getNewReport(String startTimestamp,String endTimestamp);

    /**
     * 生成日度报告
     * @param dateString 日期字符串，指定具体日期，如2022-01-01
     * @return 0 如果选定的日期内数据不为空，报告正常生成；-1 出错;0 指定日期内数据为空
     */
    int generateDailyReport(String dateString);

    int generateHourlyReport(String dateString);

}
