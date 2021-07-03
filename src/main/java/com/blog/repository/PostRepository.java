package com.blog.repository;

import com.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "Select * from post p where id in (Select pt.post_id from post_tag pt where pt.tag_id in " +
            "(Select id from tag where name = :criteria)) " +
            "UNION " +
            "SELECT * FROM post WHERE author LIKE %:criteria% OR content LIKE %:criteria% OR title LIKE %:criteria%",
            nativeQuery = true)
    List<Post> getPostByName(@Param("criteria") String criteria);

    @Query(value = "Select * from post p where p.id in (Select post_id from post_tag where tag_id = :tagId)",
            nativeQuery = true)
    List<Post> getPostByTag(@Param("tagId") Long tagId);

    @Query(value = "select * from post where created_at BETWEEN :beginDate AND :finishDate", nativeQuery = true)
    List<Post> getPostByDate(@Param("beginDate") Date beginDate, @Param("finishDate") Date finishDate);

    @Query(value = "select * from post where created_at >= :beginDate", nativeQuery = true)
    List<Post> getPostByStartDate(@Param("beginDate") Date beginDate);

    @Query(value = "select * from post where created_at <= :finishDate", nativeQuery = true)
    List<Post> getPostByEndDate(@Param("finishDate") Date finishDate);
}
