package bg.sofia.uni.fmi.mjt.escaperoom;

import bg.sofia.uni.fmi.mjt.escaperoom.exception.PlatformCapacityExceededException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.TeamNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.room.EscapeRoom;
import bg.sofia.uni.fmi.mjt.escaperoom.room.Review;
import bg.sofia.uni.fmi.mjt.escaperoom.team.Team;

public class EscapeRoomPlatform implements EscapeRoomAdminAPI, EscapeRoomPortalAPI {

    private static final double COEFFICIENT_POINTS_ACHIEVEMENT_FIRST = 0.5;
    private static final double COEFFICIENT_POINTS_ACHIEVEMENT_SECOND = 0.75;
    final private Team[] teams;
    final private int maxCapacity;
    private int currentCapacity;
    final private EscapeRoom[] rooms;

    public EscapeRoomPlatform(Team[] teams, int maxCapacity) {
        this.teams = teams;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
        this.rooms = new EscapeRoom[maxCapacity];
    }

    private int positionRoomObject(EscapeRoom room) {
        int index = -1;

        for (int i = 0; i < this.currentCapacity; i++) {
            if (this.rooms[i].equals(room)) {
                index = i;
                break;
            }
        }

        return index;
    }

    private int positionRoomTitle(String title) {
        int index = -1;

        for (int i = 0; i < this.currentCapacity; i++) {
            if (this.rooms[i].getName().equals(title)) {
                index = i;
                break;
            }
        }

        return index;
    }

    private int positionTeamTitle(String title) {
        int index = -1;

        for (int i = 0; i < this.teams.length; i++) {
            if (this.teams[i].getName().equals(title)) {
                index = i;
                break;
            }
        }

        return index;
    }

    /**
     * Adds a new escape room to the platform.
     *
     * @param room the escape room to be added.
     * @throws IllegalArgumentException          if room is null.
     * @throws PlatformCapacityExceededException if the maximum number of escape rooms has already been reached.
     * @throws RoomAlreadyExistsException        if the specified room already exists in the platform.
     */
    @Override
    public void addEscapeRoom(EscapeRoom room) throws RoomAlreadyExistsException {
        if (room == null) {
            throw new IllegalArgumentException("Given room is null");
        }
        if (this.currentCapacity == this.maxCapacity) {
            throw new PlatformCapacityExceededException("Maximum number of escape rooms has already been reached");
        }

        int roomPosition = positionRoomObject(room);

        if (roomPosition > -1) {
            throw new RoomAlreadyExistsException("The specified room already exists in the platform");
        }

        this.rooms[this.currentCapacity] = room;
        ++this.currentCapacity;
    }

    /**
     * Removes the escape room with the specified name from the platform.
     *
     * @param roomName the name of the escape room to be removed.
     * @throws IllegalArgumentException if the room name is null, empty or blank.
     * @throws RoomNotFoundException    if the platform does not contain an escape room with the specified name.
     */
    @Override
    public void removeEscapeRoom(String roomName) throws RoomNotFoundException {

        if (roomName == null || roomName.isBlank() || roomName.isEmpty()) {
            throw new IllegalArgumentException("Thr room name is null, empty or blank");
        }

        int position = positionRoomTitle(roomName);

        if (position == -1) {
            throw new RoomNotFoundException("The platform does not contain an escape room with the specified name");
        }

        for (int i = position; i < this.currentCapacity - 1; i++) {
            this.rooms[i] = this.rooms[i + 1];
        }

        this.rooms[currentCapacity] = null;
        --this.currentCapacity;
    }

    /**
     * Returns all escape rooms contained in the platform.
     */
    @Override
    public EscapeRoom[] getAllEscapeRooms() {

        EscapeRoom[] fulfilled = new EscapeRoom[this.currentCapacity];
        for (int i = 0; i < this.currentCapacity; i++) {
            fulfilled[i] = this.rooms[i];
        }
        return fulfilled;
    }

    /**
     * Registers a team achievement: escaping a room for the specified time.
     *
     * @param roomName   the name of the escape room.
     * @param teamName   the name of the team.
     * @param escapeTime the escape time in minutes.
     * @throws IllegalArgumentException if the room name or the team name is null, empty or blank,
     *                                  or if the escape time is negative, zero or bigger than the maximum time
     *                                  to escape for the specified room.
     * @throws RoomNotFoundException    if the platform does not contain an escape room with the specified name.
     */
    @Override
    public void registerAchievement(String roomName, String teamName, int escapeTime) throws RoomNotFoundException,
        TeamNotFoundException {

        if (roomName == null || teamName == null || roomName.isBlank() || teamName.isBlank() || roomName.isEmpty()
            || teamName.isEmpty() || escapeTime <= 0) {
            throw new IllegalArgumentException("The room name or the team name is null, empty or blank " +
                "or the escape time is non-positive");
        }

        int positionRoom = this.positionRoomTitle(roomName);

        if (positionRoom == -1) {
            throw new RoomNotFoundException("The platform does not contain an escape room with the specified name");
        }

        if (escapeTime > this.rooms[positionRoom].getMaxTimeToEscape()) {
            throw new IllegalArgumentException("The escape time is bigger than the maximum time to escape" +
                " for the specified room");
        }

        int positionTeam = this.positionTeamTitle(teamName);

        if (positionTeam == -1) {
            throw new TeamNotFoundException("The platform does not contain a team with the specified name");
        }

        int pointsToAdd = 0;
        pointsToAdd += this.rooms[positionRoom].getDifficulty().getRank();

        if (escapeTime <= COEFFICIENT_POINTS_ACHIEVEMENT_FIRST * this.rooms[positionRoom].getMaxTimeToEscape()) {
            pointsToAdd += 2;
        } else if (escapeTime <= COEFFICIENT_POINTS_ACHIEVEMENT_SECOND *
            this.rooms[positionRoom].getMaxTimeToEscape()) {
            pointsToAdd += 1;
        }

        this.teams[positionTeam].updateRating(pointsToAdd);
    }

    /**
     * Returns the escape room with the specified name.
     *
     * @param roomName the name of the escape room.
     * @return the escape room with the specified name.
     * @throws IllegalArgumentException if the room name is null, empty or blank
     * @throws RoomNotFoundException    if the platform does not contain an escape room with the specified name.
     */
    @Override
    public EscapeRoom getEscapeRoomByName(String roomName) throws RoomNotFoundException {

        if (roomName == null || roomName.isEmpty() || roomName.isBlank()) {
            throw new IllegalArgumentException("The room name is null, empty or blank");
        }

        int position = this.positionRoomTitle(roomName);

        if (position == -1) {
            throw new RoomNotFoundException("The platform does not contain an escape room with the specified name");
        }

        return this.rooms[position];
    }

    /**
     * Adds a review for the escape room with the specified name.
     *
     * @param roomName the name of the escape room.
     * @param review
     * @throws IllegalArgumentException if the room name is null, empty or blank, or if the review is null
     * @throws RoomNotFoundException    if the platform does not contain an escape room with the specified name.
     */
    @Override
    public void reviewEscapeRoom(String roomName, Review review) throws RoomNotFoundException {
        if (roomName == null || roomName.isEmpty() || roomName.isBlank() || review == null) {
            throw new IllegalArgumentException("The room name is null, empty or blank, or the review is null");
        }

        int position = this.positionRoomTitle(roomName);

        if (position == -1) {
            throw new RoomNotFoundException("The platform does not contain an escape room with the specified name");
        }

        this.rooms[position].addReview(review);
    }

    /**
     * Returns all reviews for the escape room with the specified name,
     * in the order they have been added - from oldest to newest.
     * If there are no reviews, returns an empty array.
     *
     * @param roomName the name of the escape room.
     * @return the reviews for the escape room with the specified name
     * @throws IllegalArgumentException if the room name is null, empty or blank, or if the review is null
     * @throws RoomNotFoundException    if the platform does not contain an escape room with the specified name.
     */
    @Override
    public Review[] getReviews(String roomName) throws RoomNotFoundException {

        if (roomName == null || roomName.isEmpty() || roomName.isBlank()) {
            throw new IllegalArgumentException("The room name is null, empty or blank");
        }

        int position = positionRoomTitle(roomName);

        if (position == -1) {
            throw new RoomNotFoundException("The platform does not contain an escape room with the specified name");
        }

        return this.rooms[position].getReviews();
    }

    /**
     * Returns the team with the highest rating. For each room successfully escaped (within the maximum
     * escape time), a team gets points equal to the room difficulty rank (1-4), plus bonus for fast escape:
     * +2 points for escape time less than or equal to 50% of the maximum escape time, or
     * +1 points for escape time less than or equal to 75% (and more than 50%) of the maximum escape time
     * The rating of a team is equal to the sum of all points collected.
     *
     * @return the top team by rating. If there are two or more teams with same highest rating, return any of them.
     * If there are no teams in the platform, returns null.
     */
    @Override
    public Team getTopTeamByRating() {

        if (this.teams == null || this.teams.length == 0) {
            return null;
        }

        int numberOfTeams = this.teams.length;

        Team maxTeam = null;
        double maxRating = 0;

        for (Team currentTeam : this.teams) {

            if (maxRating <= currentTeam.getRating()) {
                maxRating = currentTeam.getRating();
                maxTeam = currentTeam;
            }
        }

        return maxTeam;
    }
}