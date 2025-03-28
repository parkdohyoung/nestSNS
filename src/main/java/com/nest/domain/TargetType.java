package com.nest.domain;

public enum TargetType {
        POST {
            @Override
            public void applyLikeAction(Object target, boolean isIncrease) {
                if (target instanceof Post post) {
                    if (isIncrease) post.incrementLikeCount();
                    else post.decrementLikeCount();
                }
            }
        },
        COMMENT {
            @Override
            public void applyLikeAction(Object target, boolean isIncrease) {
                if (target instanceof Comment comment) {
                    if (isIncrease) comment.incrementLikeCount();
                    else comment.decrementLikeCount();
                }
            }
        };

        public abstract void applyLikeAction(Object target, boolean isIncrease);

}

