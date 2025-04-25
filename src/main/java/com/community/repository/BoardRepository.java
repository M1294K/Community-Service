package com.community.repository;

import com.community.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    //이름(name)으로 게시판을 찾고 싶을 때 사용, 기본 CRUD는 제공 해 줌 이쯤 했으면 이제 다 아는 사실이겠지?
    Optional<Board> findByName(String name);
}
