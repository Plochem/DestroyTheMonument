package net.straxidus.dtm.game;

public enum Team {
    A,
    B;

    public Team getOtherTeam() {
        if (this == Team.A)
            return Team.B;
        return Team.A;
    }
}
