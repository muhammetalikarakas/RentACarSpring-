package com.etiya.rentACarSpring.dataAccess.abstracts;

import com.etiya.rentACarSpring.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageDao extends JpaRepository<Message,Integer> {
    //List<Message> findMessagesByLanguageIdEquals(int languageId);


    @Query(value = "select m.message_content from messages as m inner join message_keys as mk  on mk.message_key_id = m.message_key_id where m.message_key_id = ?1 and m.language_id= ?2 group by message_content ",nativeQuery = true)
    Object messageContent (int messageKeyId , int languageId );

    @Query(value = "select m.message_key_id from messages as m inner join message_keys as mk  on mk.message_key_id = m.message_key_id where mk.message_key = ?1  group by  m.message_key_id " , nativeQuery = true)
     Object getMessageKeyIdByMessageKey (String messageKey);

    @Query(value="select m.message_content from messages as m inner join message_keys as mk on m.message_key_id=mk.message_key_id where m.language_id = ?1 and mk.message_key = ?2",nativeQuery=true)
    Object getMessageByLanguageIdAndKey(int languageId,String messageKey);
}
