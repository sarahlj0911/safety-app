package com.ASUPEACLab.safetyapp.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.ASUPEACLab.safetyapp.util.DateTimeUtil;

/**
 * Class intended for the creation and manipulation of
 * walkthrough objects in the code. Intended for use
 * to improve school safety through resolution
 * of given problems.
 *
 * Updated by Travis Hawley on 4/19/19
 */
@Entity(tableName = "walkthroughs",
        foreignKeys = @ForeignKey(entity = School.class,
                parentColumns = "schoolId",
                childColumns = "schoolId"))
public class Walkthrough {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    private int walkthroughId;

    @ColumnInfo(name = "schoolId")
    private int schoolId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "percentComplete")
    private double percentComplete;

    @ColumnInfo(name = "createdDate")
    private String createdDate;

    @ColumnInfo(name = "lastUpdatedDate")
    private String lastUpdatedDate;

    @ColumnInfo(name = "isDeleted")
    private int isDeleted;

    /*
     * Intended to facilitate the creation of
     * walkthrough objects with the given name
     * parameter.
     *
     * Parameter: String name is a given string
     *            that the name variable in the
     *            walkthrough is set to.
     */
    public Walkthrough(String name) {
        this.name = name;
        percentComplete = 0.0;
        createdDate = DateTimeUtil.getDateTimeString();
        lastUpdatedDate = createdDate;
        schoolId = 1;
        isDeleted = 0;
    }

    //Getters

    /*
     * Intended to allow for the retrieval of the
     * walkthrough id.
     *
     * Returns: Int walkthroughId is the specific int value
     *          given to a walkthrough.
     */
    public int getWalkthroughId() {
        return this.walkthroughId;
    }

    /*
     * Intended to allow for the retrieval of the
     * name of the walkthrough.
     *
     * Returns: String name is the given name
     *          of the walkthrough.
     */
    public String getName() {
        return this.name;
    }

    /*
     * Intended to allow for the retrieval of the
     * level of completeness of the walkthrough.
     *
     * Returns: Double percentComplete is the specific
     *          amount of the walkthrough which has been
     *          completed.
     */
    public double getPercentComplete() {
        return this.percentComplete;
    }

    /*
     * Intended to allow for the retrieval of the
     * full date of the creation of the walkthrough.
     *
     * Returns: String createdDate is the specific
     *          date on which a walkthrough was created.
     */
    public String getCreatedDate() {
        return this.createdDate;
    }

    /*
     * Intended to allow for the retrieval of the
     * full date of the last update.
     *
     * Returns: String lastUpdatedDate is the specific
     *          date on which an update to the walkthrough
     *          was last made.
     */
    public String getLastUpdatedDate() {
        return this.lastUpdatedDate;
    }

    /*
     * Intended to allow for the retrieval of the
     * school id.
     *
     * Returns: Int schoolId is the specific int value
     *          given to a school.
     */
    public int getSchoolId() {
        return this.schoolId;
    }

    /*
     * Intended to allow for checking the deletion
     * status of a walkthrough.
     *
     * Returns: Int isDeleted the exact value of the
     *          isDeleted variable rather than a
     *          true or false value.
     */
    public int getIsDeleted() {
        return this.isDeleted;
    }

    /*
     * Intended to allow for checking the specific date
     * without the time of a given date.
     *
     * Parameter: String date is a given date to be cut
     *            up in order to find only the date
     *            without including the time.
     *
     * Returns: String of the date without time.
     */
    public String getDate(String date) {
        String[] tmp = date.split(" ");
        String temp = tmp[1] + " " + tmp[2] + ", " + tmp[tmp.length - 1];
        return temp;
    }

    /*
     * Intended to allow for checking the specific time
     * of a given date.
     *
     * Parameter: String date is a given date to be cut
     *            up in order to find only the time of
     *            the date rather than the whole of it.
     *
     * Returns: String of the hour in AM and PM format.
     */
    public String getTime(String date) {
        String[] tmp = date.split(" ");
        int hour = Integer.parseInt(tmp[3].substring(0, 2));
        if (hour < 12) {
            return tmp[3].substring(0, 5) + " AM";
        } else if (hour == 12) {
            return tmp[3].substring(0, 5) + " PM";
        } else {
            return hour - 12 + tmp[3].substring(2, 5) + " PM";
        }
    }

    /*
     * Intended to allow for checking the deletion
     * status of a walkthrough.
     *
     * Returns: Boolean true if walkthrough is deleted
     *          false if it is not.
     */
    public boolean isDeleted() {
        return isDeleted == 1;
    }

    //Setters

    /*
     * Sets the walkthroughId variable for the walkthrough
     * to the given value.
     *
     * Parameter: Int walkthroughId is a given integer value
     *            that the walkthroughId variable in the
     *            walkthrough is set to.
     */
    public void setWalkthroughId(int walkthroughId) {
        this.walkthroughId = walkthroughId;
    }

    /*
     * Sets the name variable for the walkthrough
     * to the given string.
     *
     * Parameter: String name is a given string
     *            that the name variable in the
     *            walkthrough is set to.
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * Sets the percentComplete variable for the walkthrough
     * to the given value.
     *
     * Parameter: Double percentComplete is a given double value
     *            that the percentComplete variable in the
     *            walkthrough is set to.
     */
    public void setPercentComplete(double percentComplete) {
        this.percentComplete = percentComplete;
    }

    /*
     * Sets the createdDate variable for the walkthrough
     * to the given string.
     *
     * Parameter: String createdDate is a given string
     *            that the createdDate variable in the
     *            walkthrough is set to.
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /*
     * Sets the lastUpdatedDate variable for the walkthrough
     * to the given string.
     *
     * Parameter: String lastUpdatedDate is a given string
     *            that the lastUpdatedDate variable in the
     *            walkthrough is set to.
     */
    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    /*
     * Sets the schoolId variable for the walkthrough
     * to the given value.
     *
     * Parameter: Int schoolId is a given integer value
     *            that the schoolId variable in the
     *            walkthrough is set to.
     */
    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    /*
     * Sets the isDeleted variable for the walkthrough
     * to the given value.
     *
     * Parameter: Int isDeleted is a given integer value
     *            that the isDeleted variable in the
     *            walkthrough is set to.
     */
    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    /*
     * Intended to allow for walkthroughs to be
     * checked for completeness.
     *
     * Returns: Boolean false if if percentage complete
     *          is equal to 100% and true if it is not
     */
    public boolean isInProgress() {
        int complete = ((int) percentComplete);
        return complete != 100;
    }

    /*
    * Intended to allow for direct comparison of
    * walkthrough objects.
    *
    * Parameter: Object o is a given walkthrough object
    *            that is compared to the walkthrough
    *            object that equals is called on.
    *
    * Returns: Boolean true if they have the same name
    *          and id false if they do not for either
    *          of the two
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Walkthrough that = (Walkthrough) o;

        if (schoolId != that.schoolId) return false;
        return name.equals(that.name);
    }

    /*
     * Intended to create a hash code for the school id
     * and name.
     *
     * Returns: Integer result which is the result of
     *          the school id being multiplied by 31
     *          and then having the string hash code
     *          of the name added to it.
     */
    @Override
    public int hashCode() {
        int result = schoolId;
        result = 31 * result + name.hashCode();
        return result;
    }

    /*
     * Intended to allow for walkthrough objects
     * to be output as strings for easier
     * examination of the contents and
     * transfer to other formats.
     *
     * Returns: String that lists the walkthrough id,
     *          school id, name, percentage of walkthrough
     *          complete, date it was created, date it was
     *          most recently updated, and whether or not
     *          it has been deleted
     */
    @Override
    public String toString() {
        return "Walkthrough{" +
                "walkthroughId=" + walkthroughId +
                ", schoolId=" + schoolId +
                ", name='" + name + '\'' +
                ", percentComplete=" + percentComplete +
                ", createdDate='" + createdDate + '\'' +
                ", lastUpdatedDate='" + lastUpdatedDate + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}

