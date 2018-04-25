package com.android.xjq.model.injury;

import com.android.banana.commlib.bean.liveScoreBean.TeamImageUrlUtils;
import com.android.xjq.bean.injury.FootballInjuryBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DaChao on 2017/12/1.
 */

public class FootballPlayer {

    private String time;

    private String matchName;

    private int isHost;

    private long teamId;

    private String teamName;

    private String playerName;

    private String positionInfo;

    private String positionName;

    private int positionValue;

    private int matchIndex;

    private int score;

    private String status;

    private String statusInfo;

    private String guestLogoUrl;

    private String homeLogoUrl;

    public String getGuestLogoUrl() {
        return guestLogoUrl;
    }

    public void setGuestLogoUrl(String guestLogoUrl) {
        this.guestLogoUrl = guestLogoUrl;
    }

    public String getHomeLogoUrl() {
        return homeLogoUrl;
    }

    public void setHomeLogoUrl(String homeLogoUrl) {
        this.homeLogoUrl = homeLogoUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public int getIsHost() {
        return isHost;
    }

    public void setIsHost(int isHost) {
        this.isHost = isHost;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPositionInfo() {
        return positionInfo;
    }

    public void setPositionInfo(String positionInfo) {
        this.positionInfo = positionInfo;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public int getPositionValue() {
        return positionValue;
    }

    public void setPositionValue(int positionValue) {
        this.positionValue = positionValue;
    }

    public int getMatchIndex() {
        return matchIndex;
    }

    public void setMatchIndex(int matchIndex) {
        this.matchIndex = matchIndex;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    public static List<FootballPlayer> parseBean(FootballInjuryBean bean) {
        List<FootballPlayer> footballPlayers = new ArrayList<>();
        for (FootballInjuryBean.Match match : bean.raceInfoAndInjuryList) {
            footballPlayers.addAll(parseMatch(match));
        }
        return footballPlayers;
    }

    private static List<FootballPlayer> parseMatch(FootballInjuryBean.Match match) {
        List<FootballPlayer> footballPlayers = new ArrayList<>();
        footballPlayers.addAll(parseTeam(1, match));
        footballPlayers.addAll(parseTeam(0, match));
        return footballPlayers;
    }

    private static List<FootballPlayer> parseTeam(int isHost, FootballInjuryBean.Match match) {
        List<FootballPlayer> footballPlayers = new ArrayList<>();
        List<FootballInjuryBean.Match.Player> players;
        if (isHost == 1) {
            players = match.hlist;
        } else {
            players = match.glist;
        }
        for (FootballInjuryBean.Match.Player player : players) {
            FootballPlayer footballPlayer = new FootballPlayer();
            setMatchAndTeamInfo(footballPlayer, isHost, match);
            parsePlayer(player, footballPlayer);
            footballPlayers.add(footballPlayer);
        }
        if (footballPlayers.size() == 0) {
            FootballPlayer footballPlayer = new FootballPlayer();
            setMatchAndTeamInfo(footballPlayer, isHost, match);
            footballPlayers.add(footballPlayer);
        }
        return footballPlayers;
    }

    private static void setMatchAndTeamInfo(FootballPlayer footballPlayer, int isHost, FootballInjuryBean.Match match) {
        footballPlayer.setTime(match.gs);
        footballPlayer.setMatchName(match.mn);
        footballPlayer.setIsHost(isHost);
        if (isHost == 1) {
            footballPlayer.setTeamId(match.htid);
            footballPlayer.setTeamName(match.htn);
            footballPlayer.setHomeLogoUrl(getFTHomeLogoUrl(match.htid));
        } else {
            footballPlayer.setTeamId(match.gtid);
            footballPlayer.setTeamName(match.gtn);
            footballPlayer.setGuestLogoUrl(getFTGuestLogoUrl(match.gtid));
        }
    }

    private static void parsePlayer(FootballInjuryBean.Match.Player player, FootballPlayer footballPlayer) {
        footballPlayer.setPlayerName(player.pn);
        footballPlayer.setPositionName((String) player.pos.get("name"));
        footballPlayer.setPositionInfo((String) player.pos.get("message"));
        footballPlayer.setPositionValue(((Double) player.pos.get("value")).intValue());
        footballPlayer.setMatchIndex(player.po);
        footballPlayer.setScore(player.goal);
        footballPlayer.setStatus(player.status);
        footballPlayer.setStatusInfo(player.detail);
    }

    public static String getFTGuestLogoUrl(long teamId) {
        return TeamImageUrlUtils.getFTGuestLogoUrl(teamId);
    }


    public static String getFTHomeLogoUrl(long teamId) {
        return TeamImageUrlUtils.getFTHomeLogoUrl(teamId);
    }

}
