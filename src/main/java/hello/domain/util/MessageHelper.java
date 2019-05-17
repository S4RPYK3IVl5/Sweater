package hello.domain.util;

import hello.domain.User;

public abstract class MessageHelper {
    public static String getAuthorName(User author)
    {
        return author != null ? author.getUsername() : "<hone>";
    }
}
