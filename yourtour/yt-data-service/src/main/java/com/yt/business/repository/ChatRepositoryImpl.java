package com.yt.business.repository;

import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yt.business.CrudAllInOneOperateImpl;
import com.yt.business.bean.ChatJoinHistoryBean;
import com.yt.business.bean.ChatMessageBean;
import com.yt.business.bean.ChatSessionBean;
import com.yt.business.bean.PlaceBean;
import com.yt.business.bean.RouteMainBean;
import com.yt.business.bean.UserProfileBean;
import com.yt.business.neo4j.repository.ChatBeanRepository;

@Component
public class ChatRepositoryImpl extends CrudAllInOneOperateImpl implements
		ChatRepository {
	private static final Log LOG = LogFactory.getLog(ChatRepositoryImpl.class);

	@Autowired
	private ChatBeanRepository repository;

	public ChatRepositoryImpl() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.business.repository.ChatRepository#openPlaceChatRoom(long,
	 * java.lang.String)
	 */
	@Override
	public ChatSessionBean openPlaceChatRoom(long placeId, String operator)
			throws Exception {
		ChatSessionBean room = null;
		String roomNo = String.format("p%d", placeId);
		try {
			room = (ChatSessionBean) super.get(ChatSessionBean.class, "roomNo",
					roomNo);
			if (room == null) {
				// 聊天室不存在，为目的地创建一个聊天室
				// TODO 未来还需要根据目的地关系进行聊天室合并
				PlaceBean place = (PlaceBean) super.get(PlaceBean.class,
						placeId);
				if (place == null) {
					// 指定的目的地不存在，不能创建目的地聊天室
					throw new Exception(
							String.format(
									"The place(id=%d) not exist, can not create the place chat room.",
									placeId));
				}
				room = new ChatSessionBean();
				room.setChatRoomNo(roomNo);
				room.setChatRoomUrl(String.format("/ws/chat/place/%s", roomNo));
				room.setChatRoomType(ChatSessionBean.CHAT_TYPE_PLACE);
				room.setPlace(place);
				room.setRoute(null);
				super.save(room, true, operator);
			}
			return room;
		} catch (Exception ex) {
			if (LOG.isErrorEnabled()) {
				LOG.error(String.format("Open the place chat room(%s) error.",
						roomNo), ex);
			}
			throw ex;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.business.repository.ChatRepository#openRouteChatRoom(long,
	 * java.lang.String)
	 */
	@Override
	public ChatSessionBean openRouteChatRoom(long routeId, String operator)
			throws Exception {
		ChatSessionBean room = null;
		String roomNo = String.format("r%d", routeId);
		try {
			room = (ChatSessionBean) super.get(ChatSessionBean.class, "roomNo",
					roomNo);
			if (room == null) {
				// 聊天室不存在，为行程创建一个聊天室
				RouteMainBean route = (RouteMainBean) super.get(
						RouteMainBean.class, routeId);
				if (route == null) {
					// 指定的行程不存在，不能创建行程聊天室
					throw new Exception(
							String.format(
									"The route(id=%d) not exist, can not create the route chat room.",
									routeId));
				}
				room = new ChatSessionBean();
				room.setChatRoomNo(roomNo);
				room.setChatRoomUrl(String.format("/ws/chat/place/%s", roomNo));
				room.setChatRoomType(ChatSessionBean.CHAT_TYPE_ROUTE);
				room.setPlace(null);
				room.setRoute(route);
				super.save(room, true, operator);
			}
			return room;
		} catch (Exception ex) {
			if (LOG.isErrorEnabled()) {
				LOG.error(String.format("Open the route chat room(%s) error.",
						roomNo), ex);
			}
			throw ex;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yt.business.repository.ChatRepository#openDynamicChatRoom(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public ChatSessionBean openDynamicChatRoom(String roomNo, String operator)
			throws Exception {
		ChatSessionBean room = null;
		try {
			if (roomNo != null && !roomNo.isEmpty()) {
				room = (ChatSessionBean) super.get(ChatSessionBean.class,
						"roomNo", roomNo);
			}
			if (room == null) {
				// 聊天室不存在，为创建一个动态聊天室
				room = new ChatSessionBean();
				roomNo = String.format("d%s", UUID.randomUUID().toString());
				room.setChatRoomNo(roomNo);
				room.setChatRoomUrl(String.format("/ws/chat/place/%s", roomNo));
				room.setChatRoomType(ChatSessionBean.CHAT_TYPE_DYNAMIC);
				room.setPlace(null);
				room.setRoute(null);
				super.save(room, true, operator);
			}
			return room;
		} catch (Exception ex) {
			if (LOG.isErrorEnabled()) {
				LOG.error(String.format(
						"Open the dynamic chat room(%s) error.", roomNo), ex);
			}
			throw ex;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yt.business.repository.ChatRepository#getChatters(java.lang.String)
	 */
	@Override
	public List<UserProfileBean> getChatters(String roomNo) throws Exception {
		return repository.getChatters(roomNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.business.repository.ChatRepository#getTopNChatRooms(int)
	 */
	@Override
	public List<ChatSessionBean> getTopNChatRooms(int n) throws Exception {
		return repository.getHotterChatRooms(0, n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.business.repository.ChatRepository#getTopNPlaceChatRooms(int)
	 */
	@Override
	public List<ChatSessionBean> getTopNPlaceChatRooms(int n) throws Exception {
		return repository.getHotterChatRoomsByType(
				ChatSessionBean.CHAT_TYPE_PLACE, 0, n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.business.repository.ChatRepository#getTopNRouteChatRooms(int)
	 */
	@Override
	public List<ChatSessionBean> getTopNRouteChatRooms(int n) throws Exception {
		return repository.getHotterChatRoomsByType(
				ChatSessionBean.CHAT_TYPE_ROUTE, 0, n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yt.business.repository.ChatRepository#getTopNDynamicChatRooms(int)
	 */
	@Override
	public List<ChatSessionBean> getTopNDynamicChatRooms(int n)
			throws Exception {
		return repository.getHotterChatRoomsByType(
				ChatSessionBean.CHAT_TYPE_DYNAMIC, 0, n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yt.business.repository.ChatRepository#getUnreadMessageCount(java.
	 * lang.String, long)
	 */
	@Override
	public long getUnreadMessageCount(String roomNo, long userId)
			throws Exception {
		return repository.getUnreadMessageCount(roomNo, userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yt.business.repository.ChatRepository#getUnreadMessages(java.lang
	 * .String, long)
	 */
	@Override
	public List<ChatMessageBean> getUnreadMessages(String roomNo, long userId)
			throws Exception {
		return repository.getUnreadMessages(roomNo, userId, 0, -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yt.business.repository.ChatRepository#getUnreadMessages(java.lang
	 * .String, long, int, int)
	 */
	@Override
	public List<ChatMessageBean> getUnreadMessages(String roomNo, long userId,
			int skip, int limit) throws Exception {
		return repository.getUnreadMessages(roomNo, userId, skip, limit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yt.business.repository.ChatRepository#getHistoryChatRooms(long)
	 */
	@Override
	public List<ChatSessionBean> getHistoryChatRooms(long userId)
			throws Exception {
		return repository.getHistoryChatRooms(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yt.business.repository.ChatRepository#getNewestChatJoinRecord(long)
	 */
	@Override
	public ChatJoinHistoryBean getNewestChatJoinRecord(long userId)
			throws Exception {
		return repository.getNewestChatJoinRecord(userId);
	}

}
