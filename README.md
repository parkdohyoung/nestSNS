# Nest SNS 
> 👋 Nest Sns의 API 정보 입니다.
> 

# 프로젝트 정보

## 제작 기간

> 2025-03-01 ~ 2025-03-28
> 

## 참여 인원

> 박도형( Back)
> 

## 기술 스택

> JAVA ,  Springboot , JPA
> 

## 기능
| 객체 | 기능  | Method | URL | Param / PathVariable | Request Body |
| --- | ---- | --- | --- | --- | --- |
| 회원 | 회원 가입  | POST | /api/account/join |  | String email, String name,String password |
|  | 이메일 중복 확인 | Get | /api/account/duplicate-email | String email  |  |
|  | 사용자 닉네임 중복 확인 | Get | /api/account/duplicate-name | String name |  |
|  | 이메일 인증 | Get | /api/auth/verify-email | String token |  |
|  | 개인정보 조회 | Get | /api/account/edit | JWT Token |  |
|  | 개인정보 수정 | Post | /api/account/edit | JWT Token | String name, String email, String profileImgPath, String profileMessage | |
|  | 회원 탈퇴 | Post | /api/account/withdraw | JWT Token |  |
| 팔로우 | 팔로우 추가 | Get | /api/follow/add | JWT Token Long followingId |  |
|  | 나의 팔로우 리스트 | Get | /api/follow | JWT Token |  |
|  | 언팔로우 | Get | /api/follow/unfollow | JWT Token Long followingId |  |
| 포스트 | 계정별 포스트 보기 | Get | /api/post/{targetAccountId} | JWT Token Long targetAccountId int page (default = 0) int size (default = 10) |  |
|  | 팔로우 계정의 포스트 보기 | Get | /api/post | JWT Token int page int size |  |
|  | 포스트 작성 (글) | Post | /api/post/new | JWT Token | String message | |
|  | 포스트 수정(조회) | Get | /api/post/edit/{postId} | JWT Token Long postId |  |
|  | 포스트 글 수정  | Post | /api/post/edit/{postId} | JWT Token Long postId | String message |
|  | 포스트 삭제 (전체) | Delete | /api/post/delete/image | JWT Token Long postId |  |
|  | 포스트 이미지 작성/수정  | Post | /api/post/edit/image | JWT Token Long postId MultipartFile image |  |
|  | 포스트 이미지 삭제 | Delete | /api/post/delete | JWT Token Long postId |  |
|  | 관심 포스트 저장하기 | Get | /api/saved-post/add | JWT Token Long postId |  |
|  | 관심 포스트 삭제하기 | Delete | /api/saved-post/delete | JWT Token Long postId |  |
|  | #키워드로 포스트 검색 | Get | /api/post/search | JWT Token String keyword int page (default = 0) int size (default = 10) |  |
| 댓글 | 부모 댓글 보기 | Get | /api/comments/post | JWT Token Long postId |  |
|  | 자식 댓글 보기 | Get | /api/comments/comment | JWT Token Long commentId |  |
|  | 댓글 작성 | Get | /api/comments/new | JWT Token | Long postId String commentMessage Long parentCommentId; | |
|  | 댓글 수정(조회) | Get | /api/comments/edit/{commentId} | JWT Token Long commentId |  |
|  | 댓글 수정 | Post | /api/comments/edit/{commentId} | JWT Token  | Long PostId String commentMessage | |
|  | 댓글 삭제 | Delete | /api/comments/delete/{comment} | JWT Token Long commentId |  | 
| 포스트, 댓글 공통 | 좋아요/ 좋아요 취소 | Get | /api/likes | JWT Token Long targetId TargetType (POST, COMMENT) Boolean isIncrease |  |
| #키워드, @언급 | 안 읽은 언급 알림 조회 | Get | /api/notification/unread | JWT Token |  |
|  | 언급 읽음 처리 | Get | /api/notification/read | JWT Token Long notificationId |  |

## ERD

![nest.png](https://github.com/parkdohyoung/nestSNS/blob/main/nest.png)

