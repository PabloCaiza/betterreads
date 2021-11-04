package io.javabrains.betterreads.author;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

@Table(value = "author_by_id")
@Data
@Builder
public class Author {

    @Id
    @PrimaryKeyColumn(name = "author_id",ordinal = 0,type = PrimaryKeyType.PARTITIONED)
    private String id;
    @Column("author_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String name;
    @Column("personal_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String personalName;
}
