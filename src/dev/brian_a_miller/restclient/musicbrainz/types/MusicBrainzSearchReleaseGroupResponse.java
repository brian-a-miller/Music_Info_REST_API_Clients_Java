package dev.brian_a_miller.restclient.musicbrainz.types;

import com.google.gson.annotations.SerializedName;

public record MusicBrainzSearchReleaseGroupResponse(
        int count, int offset,
        @SerializedName("release-groups") MusicBrainzReleaseGroup[] release_groups) {
}
