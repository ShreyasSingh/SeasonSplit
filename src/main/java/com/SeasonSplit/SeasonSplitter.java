package com.SeasonSplit;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SeasonSplitter {

	private LocalDate minStartDateOfHotelMSG = LocalDate.now();
	private LocalDate maxEndDateOfHotelMSG = LocalDate.now();

	public SeasonSplitter() {}


	public Set<Season> splitSeason(final List<Season> previouslySplittedSeasons, final Set<Season> newSeasons) {
		// fetchMinStartDateANDMaxEndDate(newSeasons);

		for (Season currentSeason : newSeasons) {
			Boolean noverlap = true;
			int i = 0;
			if (previouslySplittedSeasons.isEmpty()) {
				previouslySplittedSeasons.add(currentSeason);
				i++;
				continue;
			}
			while (i < previouslySplittedSeasons.size()) {
				Season previousSeason = previouslySplittedSeasons.get(i);

				//normal 3 season split
				if (currentSeason.getSeasonStartDate().isAfter(previousSeason.getSeasonStartDate())
						&& currentSeason.getSeasonEndDate().isBefore(previousSeason.getSeasonEndDate())) {
					LocalDate previousSeasonEndDate = previousSeason.getSeasonEndDate();
					previousSeason.setSeasonEndDate(currentSeason.getSeasonStartDate().minusDays(1));
					previouslySplittedSeasons.add(currentSeason);
					i++;
					Season newSplittedSeason = new Season(1, previousSeason.getHotelMSG(),
							currentSeason.getSeasonEndDate().plusDays(1), previousSeasonEndDate,
							previousSeason.getSeasonBAR());
					previouslySplittedSeasons.add(newSplittedSeason);
					i++;
					noverlap = false;
					break;
				}

				//overlap at end of previous season
				if (currentSeason.getSeasonStartDate().isAfter(previousSeason.getSeasonStartDate())
						&& !currentSeason.getSeasonStartDate().isAfter(previousSeason.getSeasonEndDate())) {

					previousSeason.setSeasonEndDate(currentSeason.getSeasonStartDate().minusDays(1));
					previouslySplittedSeasons.add(currentSeason);
					noverlap = false;

				}

				//complete overlap of season
				if (!currentSeason.getSeasonStartDate().isAfter(previousSeason.getSeasonStartDate())
						&& !currentSeason.getSeasonEndDate().isBefore(previousSeason.getSeasonEndDate())) {
					previouslySplittedSeasons.remove(previousSeason);
					previouslySplittedSeasons.add(i, currentSeason);

				}

				//overlap at start of previous season
				if (currentSeason.getSeasonStartDate().isBefore(previousSeason.getSeasonStartDate())
						&& !currentSeason.getSeasonEndDate().isBefore(previousSeason.getSeasonStartDate())) {
					previousSeason.setSeasonStartDate(currentSeason.getSeasonEndDate().plusDays(1));
					previouslySplittedSeasons.add(i, currentSeason);
					noverlap = false;
				}
				//no overlap
				if (currentSeason.getSeasonStartDate().isAfter(previousSeason.getSeasonEndDate()) && noverlap) {

					previouslySplittedSeasons.add(currentSeason);
					noverlap = false;

				}
				i++;

			}

		}
		return new TreeSet<Season>(previouslySplittedSeasons);

	}

}
