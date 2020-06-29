package rk.projects.titan.commons.groupmembership;

import java.util.concurrent.CompletableFuture;

public interface GroupMembershipManager {

  /**
   * @param groupId Unique Id of the group.
   * @return Number of members in the group.
   */
  CompletableFuture<Integer> getCount(String groupId);

  /**
   * @param groupId  Unique Id of the group.
   * @param memberId Unique Id of the member.
   * @return CompletionStage representing successful update.
   */
  CompletableFuture<Void> update(String groupId, String memberId);
}
