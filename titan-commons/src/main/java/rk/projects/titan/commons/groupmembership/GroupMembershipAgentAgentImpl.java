package rk.projects.titan.commons.groupmembership;

import java.time.Duration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GroupMembershipAgentAgentImpl implements GroupMembershipAgent {

  private final GroupMembershipManager groupMembershipManager;
  private final String groupId;
  private final String memberId;
  private final Flux<Integer> sizePublisher;

  private static final Duration heartBeatFrequency = Duration.ofSeconds(10);

  public GroupMembershipAgentAgentImpl(final GroupMembershipManager groupMembershipManager,
      final String groupId, final String memberId) {
    this.groupMembershipManager = groupMembershipManager;
    this.groupId = groupId;
    this.memberId = memberId;
    this.sizePublisher =
        Mono.defer(() -> Mono.fromFuture(this.groupMembershipManager.getCount(this.groupId)))
            .cache(Duration.ofSeconds(10))
            .repeat();
  }

  @Override
  public void init() {
    Mono.defer(
        () -> Mono.fromFuture(this.groupMembershipManager.update(this.groupId, this.memberId)))
        .delayElement(heartBeatFrequency)
        .repeat()
        .onErrorContinue((e, r) -> {
          // log error.
          // result the heart beat sender.
        })
        .subscribe();
  }

  @Override
  public int size() {
    return this.sizePublisher.blockFirst();
  }
}
