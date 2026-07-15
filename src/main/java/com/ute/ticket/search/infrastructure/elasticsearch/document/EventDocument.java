package com.ute.ticket.search.infrastructure.elasticsearch.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "events", createIndex = true)
@Setting(settingPath = "elasticsearch/events-settings.json")
public class EventDocument {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @MultiField(
            mainField = @Field(type = FieldType.Text),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword),
                    @InnerField(suffix = "ngram", type = FieldType.Text, analyzer = "autocomplete")
            }
    )
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Keyword, index = false)
    private String bannerUrl;

    @Field(type = FieldType.Long)
    private Long organizationId;

    @MultiField(
            mainField = @Field(type = FieldType.Text),
            otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword)
    )
    private String organizationName;

    @Field(type = FieldType.Long)
    private Long venueId;

    @MultiField(
            mainField = @Field(type = FieldType.Text),
            otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword)
    )
    private String venueName;

    @MultiField(
            mainField = @Field(type = FieldType.Text),
            otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword)
    )
    private String venueCity;

    @Field(type = FieldType.Long)
    private List<Long> categoryIds;

    @Field(type = FieldType.Keyword)
    private List<String> categoryNames;

    @Field(type = FieldType.Keyword)
    private List<String> categorySlugs;

    @Field(type = FieldType.Double)
    private Double minPrice;

    @Field(type = FieldType.Double)
    private Double maxPrice;

    @Field(type = FieldType.Boolean)
    private Boolean hasAvailableTickets;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Instant publishedAt;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Instant createdAt;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Instant updatedAt;
}
