package treeProcessing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.Bucket;
import utilities.Parameter;

public class RecordSwapper {
	

	// sort the list passed by the "anonymizedCode"  value  to easily find the bucket that contains 
	public static void sortDiseaseCode(ArrayList<Bucket> listbuckt) {
		
		
		for (Bucket b : listbuckt) {
			Collections.sort(b.getRecords(), new Comparator<Map<String, String>>() {
				@Override
				public int compare(final Map<String, String> map1, final Map<String, String> map2) {
					return map1.get("anonymizedCode").compareTo(map2.get("anonymizedCode"));
				}
			});
		}
	}

	// shuffle bukets
	public static void bucketsShuffler(ArrayList<Bucket> list, String operation) {

		List<Bucket> workingbuckets = new ArrayList<>();

		// bringing only backets that should swap
		for (Bucket b : list) {
			if (b.isShouldSwapp()) {
				workingbuckets.add(b);
			}
		}
		System.out.println("bkt to swap :" + workingbuckets.size());
		System.out.println("bkt to swap :" + workingbuckets);
		// cases
		// if the the is only one bucket to be swapped we swap it with an arbitrary
		// backut from the list
		if (workingbuckets.size() == 1) {

			Bucket b1 = getBucket(list);
			Bucket b2 = workingbuckets.get(0);
			swap(b1, b2, operation);
//			b2.setShouldSwapp(false);
//			workingbuckets.remove(0);
			System.out.println("inside if");

		}
		// if there is an unpair number of buckets
		else if (workingbuckets.size() % 2 == 1 && workingbuckets.size() != 1) {
			List<Bucket> result = new ArrayList<Bucket>();
			for (int i = 0; i < workingbuckets.size() - 1; i += 2) {

				Bucket b1 = workingbuckets.get(i);
				Bucket b2 = workingbuckets.get(i + 1);

				if (b1.getRecords().get(0).get("anonymizedCode").substring(0, 3)
						.equals(b2.getRecords().get(0).get("anonymizedCode").substring(0, 3))) {

					swap(b1, getBucket(list), operation);
					swap(b2, getBucket(list), operation);

				} else {
					swap(b1, b2, operation);
				}
				b1.setShouldSwapp(false);
				b2.setShouldSwapp(false);
				result.add(b1);
				result.add(b2);

			}
			Bucket b1 = getBucket(result);
			Bucket b2 = workingbuckets.get(workingbuckets.size() - 1);
			swap(b1, b2, operation);

		}
		// if there is a pair number of buckets to be swapped
		else if (workingbuckets.size() % 2 == 0) {
			for (int i = 0; i < workingbuckets.size(); i += 2) {
				Bucket b1 = workingbuckets.get(i);
				Bucket b2 = workingbuckets.get(i + 1);

				if (b1.getRecords().get(0).get("anonymizedCode").substring(0, 3)
						.equals(b2.getRecords().get(0).get("anonymizedCode").substring(0, 3))) {

					swap(b1, getBucket(workingbuckets), operation);
					swap(b2, getBucket(workingbuckets), operation);

				} else {
					swap(b1, b2, operation);
				}

			}
		}

	}

	// this method determines buckets that should e swapped
	public static void bucketsClassifier(ArrayList<Bucket> list) {
		for (Bucket b : list) {

			if (b.getRecords().size() > 1) {

				String prefix = b.getRecords().get(0).get("anonymizedCode").substring(0, 3);
				// System.out.println(prefix);
				b.setShouldSwapp(true);
				for (int i = 1; i < b.getRecords().size(); i++) {
					if (!b.getRecords().get(i).get("anonymizedCode").startsWith(prefix)) {
						b.setShouldSwapp(false);
					}
				}
			}
		}
	}

	// this method get tow bucket and tries to swap there recodes based on the criteria passed
	private static void swap(Bucket b1, Bucket b2, String op) {

		if (op == "min") {

			Parameter.Nbchaged++;

			String temp = b1.getRecords().get(0).get(Parameter.bktId);
			b1.getRecords().get(0).put(Parameter.bktId, b2.getRecords().get(0).get(Parameter.bktId));
			b2.getRecords().get(0).put(Parameter.bktId, temp);

			String temp2 = b1.getRecords().get(0).get("anonymizedCode");
			b1.getRecords().get(0).put("anonymizedCode", b2.getRecords().get(0).get("anonymizedCode"));
			b2.getRecords().get(0).put("anonymizedCode", temp2);

		}
		if (op == "max") {

			Parameter.Nbchaged++;

			String temp = b1.getRecords().get(b1.getRecords().size() - 1).get(Parameter.bktId);
			b1.getRecords().get(b1.getRecords().size() - 1).put(Parameter.bktId,
					b2.getRecords().get(b2.getRecords().size() - 1).get(Parameter.bktId));
			b2.getRecords().get(b2.getRecords().size() - 1).put(Parameter.bktId, temp);

			String temp2 = b1.getRecords().get(b1.getRecords().size() - 1).get("anonymizedCode");
			b1.getRecords().get(b1.getRecords().size() - 1).put("anonymizedCode",
					b2.getRecords().get(b2.getRecords().size() - 1).get("anonymizedCode"));
			b2.getRecords().get(b2.getRecords().size() - 1).put("anonymizedCode", temp2);

		}
		if (op == "maxtomin") {

			Parameter.Nbchaged++;

			String temp = b1.getRecords().get(b1.getRecords().size() - 1).get(Parameter.bktId);
			b1.getRecords().get(b1.getRecords().size() - 1).put(Parameter.bktId,
					b2.getRecords().get(0).get(Parameter.bktId));
			b2.getRecords().get(0).put(Parameter.bktId, temp);

			String temp2 = b1.getRecords().get(b1.getRecords().size() - 1).get("anonymizedCode");
			b1.getRecords().get(b1.getRecords().size() - 1).put("anonymizedCode",
					b2.getRecords().get(0).get("anonymizedCode"));
			b2.getRecords().get(0).put("anonymizedCode", temp2);

		}
		if (op == "mintomax") {

			Parameter.Nbchaged++;

			String temp = b1.getRecords().get(0).get(Parameter.bktId);
			b1.getRecords().get(0).put(Parameter.bktId,
					b2.getRecords().get(b2.getRecords().size() - 1).get(Parameter.bktId));
			b2.getRecords().get(b2.getRecords().size() - 1).put(Parameter.bktId, temp);

			String temp2 = b1.getRecords().get(0).get("anonymizedCode");
			b1.getRecords().get(0).put("anonymizedCode",
					b2.getRecords().get(b2.getRecords().size() - 1).get("anonymizedCode"));
			b2.getRecords().get(b2.getRecords().size() - 1).put("anonymizedCode", temp2);
		}
	}

	// retrun a bucket in order to be swapped with an other
	private static Bucket getBucket(List<Bucket> lb) {
		Bucket b;
		int i = 100;
		do {
			i--;
			b = lb.get((int) Math.floor(Math.random() * lb.size()));
		} while (b.isShouldSwapp() && i > 0);
		return b;
	}

}
