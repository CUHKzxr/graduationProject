package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.example.demo.bean.RsiData;
import com.example.demo.bean.RsiDataWithIpv4;
import com.example.demo.bean.RsiDataWithIpv6;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RsiDataMapper extends BaseMapper<RsiData> {
    String SQL = "SELECT " +
                    "rsi_data.timestamp_ AS timestamp_ , " +
                    "rsi_data.provider AS provider, " +
                    "rsi_data.name_ AS name_, " +
                    "rsi_data.identification AS identification, " +
                    "rsi_data.query_latency_ipv4_udp AS query_latency_ipv4_udp, " +
                    "rsi_data.query_latency_ipv4_tcp AS query_latency_ipv4_tcp, " +
                    "rsi_data.query_latency_ipv6_udp AS query_latency_ipv6_udp, " +
                    "rsi_data.query_latency_ipv6_tcp AS query_latency_ipv6_tcp, " +
                    "rsi_data.status_ AS status_, " +
                    "rsi_data.source_ip_ipv4 AS source_ip_ipv4, " +
                    "rsi_data.source_ip_ipv6 AS source_ip_ipv6, " +
                    "rsi_data.correctness AS correctness, " +
                    "rsi_data.publication_latency AS publication_latency, " +
                    "rsi_route_data_ipv4.index_ AS index_, " +
                    "rsi_route_data_ipv4.address AS address " +
                "FROM rsi_data LEFT JOIN rsi_route_data_ipv4 ON " +
                        "rsi_data.timestamp_ = rsi_route_data_ipv4.timestamp_ " +
                        "AND rsi_data.provider = rsi_route_data_ipv4.provider " +
                        "AND rsi_data.name_ = rsi_route_data_ipv4.name_ " +
                        "AND rsi_data.identification = rsi_route_data_ipv4.identification " +
                    " ${ew.customSqlSegment} "+
                "ORDER BY timestamp_, provider, name_, identification, index_ ";
    public List<RsiDataWithIpv4> queryDetailIpv4(@Param(Constants.WRAPPER) QueryWrapper<RsiData> wrapper);
    public List<RsiDataWithIpv6> queryDetailIpv6(@Param(Constants.WRAPPER) QueryWrapper<RsiData> wrapper);
    //public List<RsiRouteDataIpv6> queryDetailIpv6();

}
