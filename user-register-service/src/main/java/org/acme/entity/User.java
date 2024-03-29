package org.acme.entity;


import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;


public class User {

    public Long id;
    public String userId;
    public String userPw;
    public String email;

    public User() {
    }

    public User(Long id , String userId) {
        this.id = id;
        this.userId = userId;

    }

    public User(Long id , String userId , String userPw , String email) {
        this.id = id;
        this.userId = userId;
        this.userPw = userPw;
        this.email = email;
    }


    public static Multi<User> findAll(PgPool client) {
        return client.query( "SELECT id, userId,userPw,email FROM users ORDER BY userId ASC" ).execute()
                     .onItem()
                     .transformToMulti( set -> Multi.createFrom().iterable( set ) )
                     .onItem().transform( User::from );
    }


    public static Uni<User> findById(PgPool client , Long id) {
        return client.preparedQuery( "SELECT id, userId FROM users WHERE id=$1 " ).execute( Tuple.of( id ) ).onItem()
                     .transform( RowSet::iterator ).onItem()
                     .transform( iterator -> iterator.hasNext() ? from( iterator.next() ) : null );
    }

    public static Uni<Boolean> delete(PgPool client , Long id) {
        return client.preparedQuery( "DELETE FROM users WHERE id =$1" ).execute( Tuple.of( id ) ).onItem()
                     .transform( pgRowSet -> pgRowSet.rowCount() == 1 );
    }

    private static User from(Row row) {
        return new User( row.getLong( "id" ) , row.getString( "userId" ) );
    }

    public Uni<Long> save(PgPool client) {
        return client.preparedQuery( "INSERT INTO users (userId,userPw,email) VALUES ($1,$2,$3) RETURNING id " )
                     .execute( Tuple.of( userId , userPw , email ) )
                     .onItem()
                     .transform( pgRowSet -> pgRowSet.iterator().next().getLong( "id" ) );
    }

//    public Uni<Boolean> update(PgPool client) {
//        return client.preparedQuery( "UPDATE users SET userId = $1 WHERE id = $2" ).execute( Tuple.of( userId , id ) )
//                     .onItem().transform( pgRowSet -> pgRowSet.rowCount() == 1 );
//
//    }


}
