package com.android.xjq.model.injury;

import com.android.banana.commlib.bean.liveScoreBean.TeamImageUrlUtils;
import com.android.xjq.bean.injury.BasketballInjuryBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DaChao on 2017/12/1.
 */

public class BasketballPlayer {

    private long teamId;

    private String teamName;

    private long playerId;

    private String playerName;

    private String position;

    private int matchIndex;

    private int score;

    private String status;

    private String statusInfo;

    private String teamLogoUrl;

    public String getTeamLogoUrl() {
        return teamLogoUrl;
    }

    public void setTeamLogoUrl(String teamLogoUrl) {
        this.teamLogoUrl = teamLogoUrl;
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

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public static List<BasketballPlayer> parseBean(BasketballInjuryBean bean) {
        List<BasketballPlayer> basketballPlayers = new ArrayList<>();

        if(bean.teamInjuryList!=null){
            for (int i = 0; i <bean.teamInjuryList.size() ; i++) {
                basketballPlayers.addAll(parseTeam(bean.teamInjuryList.get(i)));
            }
        }
        return basketballPlayers;
    }

    private static List<BasketballPlayer> parseTeam(BasketballInjuryBean.Team team) {
        List<BasketballPlayer> basketballPlayers = new ArrayList<>();
        for (BasketballInjuryBean.Team.Player player : team.injuryList) {
            basketballPlayers.add(parsePlayer(player));
        }
        return basketballPlayers;
    }

    private static BasketballPlayer parsePlayer(BasketballInjuryBean.Team.Player player) {
        BasketballPlayer basketballPlayer = new BasketballPlayer();
        basketballPlayer.setTeamId(player.ti);
        basketballPlayer.setTeamName(player.tn);
        basketballPlayer.setPlayerId(player.id);
        basketballPlayer.setPlayerName(player.pn);
        basketballPlayer.setPosition(player.pos);
        basketballPlayer.setMatchIndex(player.cc);
        basketballPlayer.setScore(player.as);
        basketballPlayer.setStatus(player.status);
        basketballPlayer.setStatusInfo(player.detail);
        basketballPlayer.setTeamLogoUrl(getBTLogoUrl(player.ti));
        return basketballPlayer;
    }


    public static String getBTLogoUrl(long teamId) {
        return TeamImageUrlUtils.getBTLogoUrl(teamId);
    }

}
