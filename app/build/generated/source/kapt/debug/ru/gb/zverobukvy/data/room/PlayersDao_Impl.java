package ru.gb.zverobukvy.data.room;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import ru.gb.zverobukvy.domain.entity.Player;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PlayersDao_Impl implements PlayersDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Player> __insertionAdapterOfPlayer;

  private final EntityDeletionOrUpdateAdapter<Player> __deletionAdapterOfPlayer;

  private final EntityDeletionOrUpdateAdapter<Player> __updateAdapterOfPlayer;

  public PlayersDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlayer = new EntityInsertionAdapter<Player>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `players` (`name`,`id`) VALUES (?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Player value) {
        if (value.getName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getName());
        }
        stmt.bindLong(2, value.getId());
      }
    };
    this.__deletionAdapterOfPlayer = new EntityDeletionOrUpdateAdapter<Player>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `players` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Player value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfPlayer = new EntityDeletionOrUpdateAdapter<Player>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `players` SET `name` = ?,`id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Player value) {
        if (value.getName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getName());
        }
        stmt.bindLong(2, value.getId());
        stmt.bindLong(3, value.getId());
      }
    };
  }

  @Override
  public Object insertPlayer(final Player player, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlayer.insert(player);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertPlayers(final List<Player> players,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlayer.insert(players);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePlayer(final Player player, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPlayer.handle(player);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePlayer(final Player player, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPlayer.handle(player);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPlayers(final Continuation<? super List<Player>> $completion) {
    final String _sql = "SELECT * FROM players";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Player>>() {
      @Override
      public List<Player> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<Player> _result = new ArrayList<Player>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Player _item;
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new Player(_tmpName,_tmpId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
