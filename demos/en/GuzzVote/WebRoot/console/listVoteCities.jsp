<%@ page pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/tags.jsp"%>

<g:get var="m_vote" business="bigVote" limit="id=${param.voteId}" />

<c:if test="${empty param.order}">
	<g:list var="m_cities" business="voteTerritory"
		limit="voteId=${m_vote.id}" orderBy="id asc" pageSize="200" />
</c:if>
<c:if test="${not empty param.order}">
	<g:list var="m_cities" business="voteTerritory"
		limit="voteId=${m_vote.id}" orderBy="${param.order}" pageSize="200" />
</c:if>

<g:count var="m_cityAddedVoteNum" selectPhrase="sum(addedVoteNum)"
	business="voteTerritory" limit="voteId=${m_vote.id}" />

<g:count var="m_maxCityVoteNum" business="voteTerritory"
	selectPhrase="max(voteNum)" limit="voteId=${m_vote.id}" />
<g:count var="m_maxCityAddedVoteNum" business="voteTerritory"
	selectPhrase="max(addedVoteNum)" limit="voteId=${m_vote.id}" />

<g:count var="m_cityVotePeopleBase" business="voteTerritory"
	selectPhrase="max(votePeople)" limit="voteId=${m_vote.id}" />
<c:set var="m_cityVoteNumBase"
	value="${m_maxCityVoteNum + m_maxCityAddedVoteNum }" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Manage Cities</title>
		<link rel="stylesheet" type="text/css" href="../css/style.css" />
	</head>

	<body>
		<div id="page">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td valign="top" class="right">
						<div class="cornerNav">
							${m_vote.name} &gt;&gt;
						</div>
						<div class="big_nav">
							<span><b><a
									href="listVoteItems.jsp?voteId=${m_vote.id}">Manage Vote Items</a>
							</b>
							</span>
							<span>|</span><span class="cur"><b><a
									href="listVoteCities.jsp?voteId=${m_vote.id}">Manage Cities</a>
							</b>
							</span>
							<span>|</span><span><b><a
									href="listVoteExtraProperties.jsp?voteId=${m_vote.id}">User-defined Form</a>
							</b>
							</span>
							<span>|</span><span><b><a
									href="listVoteItemGroups.jsp?voteId=${m_vote.id}">Manage Vote Questions</a>
							</b>
							</span>
							<span>|</span><span><b><a
									href="listAntiCheatPolicies.jsp?voteId=${m_vote.id}">Anti Cheat</a>
							</b>
							</span>
						</div>
						<div class="small_nav">

							<span><b><a
									href="voteCityAction.do?voteId=${m_vote.id}"
									title="">Add new area</a>
							</b>
							</span>
							<span>|</span>
							<span><b><a href="?voteId=${m_vote.id}&order=id asc">Natural Order</a>
							</b>
							</span>
							<span>|</span>
							<span><b><a
									href="?voteId=${m_vote.id}&order=votePeople desc">Vote People Order</a>
							</b>
							</span>
							<span>|</span>
							<span><b><a
									href="?voteId=${m_vote.id}&order=voteNum desc">Votes Order</a>
							</b>
							</span> &nbsp;&nbsp;

							<font color="#dd8833"><b> <c:if
										test="${m_vote.addedVoteNum > m_cityAddedVoteNum}">
     		Warning: City votes is <font color="red">less
											${m_vote.addedVoteNum - m_cityAddedVoteNum}</font> that item votes!
     	</c:if> <c:if test="${m_vote.addedVoteNum < m_cityAddedVoteNum}">
     		Warning: City votes is <font color="red">more
											${m_cityAddedVoteNum - m_vote.addedVoteNum}</font> that item votes!
     	</c:if> </b>
							</font>

						</div>

						<div class="text_sta">
							<table width="99%" class="statistics" border="0" cellspacing="0"
								cellpadding="0">
								<thead>
									<tr>
										<td style="width: 40px;">
											No.
										</td>
										<td style="width: 80px;">
											City Name
										</td>
										<td style="width: 110px;">
											Vote People
										</td>
										<td style="width: 260px;">
											Percents of Total Vote People
										</td>
										<td style="width: 110px;">
											User Votes
										</td>
										<td style="width: 110px;">
											Appended Votes
										</td>
										<td style="width: 260px;">
											Percents of Total Votes
										</td>
										<td style="width: 120px;">
											Operations
										</td>
									</tr>
								</thead>
								<c:forEach items="${m_cities}" var="m_city" varStatus="m_status">
									<tr bgcolor="#f6f6f6">
										<td>
											${m_status.index + 1}
										</td>
										<td>
											&nbsp;
											<c:out value="${m_city.name}" escapeXml="true" />
										</td>

										<td>
											${m_city.votePeople}
										</td>
										<c:if test="${m_vote.votePeople == 0}">
											<td>
												0%
											</td>
										</c:if>
										<c:if test="${m_vote.votePeople != 0}">
											<fmt:formatNumber var="m_tmp_count" maxIntegerDigits="3"
												maxFractionDigits="2" minFractionDigits="2"
												value="${m_city.votePeople * 100 / m_cityVotePeopleBase}" />
											<fmt:formatNumber var="m_tmp_count2" maxIntegerDigits="3"
												maxFractionDigits="2" minFractionDigits="2"
												value="${m_city.votePeople * 100 / m_vote.votePeople}" />
											<td valign="middle">
												<img src="../css/dot_v_line.gif" width="${m_tmp_count}px;"
													height="15px;">
												${m_tmp_count2}%
											</td>
										</c:if>

										<td>
											${m_city.voteNum}
										</td>
										<td>
											${m_city.addedVoteNum}
										</td>

										<c:if test="${m_vote.showVoteNum == 0}">
											<td>
												0%
											</td>
										</c:if>
										<c:if test="${m_vote.showVoteNum != 0}">
											<fmt:formatNumber var="m_tmp_count" maxIntegerDigits="3"
												maxFractionDigits="2" minFractionDigits="2"
												groupingUsed="false"
												value="${m_city.showVoteNum * 100 / m_cityVoteNumBase}" />
											<fmt:formatNumber var="m_tmp_count2" maxIntegerDigits="3"
												maxFractionDigits="2" minFractionDigits="2"
												groupingUsed="false"
												value="${m_city.showVoteNum * 100 / m_vote.showVoteNum}" />
											<td valign="middle">
												<img src="../css/dot_v_line.gif" width="${m_tmp_count}px;"
													height="15px;" title="Total Votes: ${m_city.showVoteNum}">
												${m_tmp_count2}%
											</td>
										</c:if>

										<td>
											System No:${m_city.id}
											<br />
											<span class="blue">[<a
												href="listCityDetailItems.jsp?cityId=${m_city.id}">Detailed Stat</a>]</span>
											<br />
											<span class="blue">[<a
												href="voteCityAction.do?id=${m_city.id}">Modify</a>]</span>
										</td>
									</tr>
								</c:forEach>
							</table>
						</div>

						<div class="footSpacing" style="height: 50px;">
							&nbsp;
						</div>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
