package dev.brian_a_miller.restclient.musicbrainz.types;

import com.google.gson.annotations.SerializedName;

public record MusicBrainzReleaseGroup(String id,
                                      @SerializedName("type-id") String type_id, String score,
                                      @SerializedName("primary-type-id") String primary_type_id,
                                      @SerializedName("first-release-date") String first_release_date,
                                      @SerializedName("primary-type") String primary_type,
                                      int count, String title
                                      /* artist-credit */
                                      /* releases */) {
}
