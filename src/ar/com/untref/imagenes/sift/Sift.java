package ar.com.untref.imagenes.sift;

import java.io.File;

import javax.swing.JOptionPane;

import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.BasicMatcher;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.MatchingUtilities;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.model.fit.RANSAC;

public class Sift {

	public static void aplicarMetodoSift(File imagen1, File imagen2, boolean esRobusto)
			throws Exception {

		MBFImage query = ImageUtilities.readMBF(imagen1);
		MBFImage target = ImageUtilities.readMBF(imagen2);

		// Se crea un motor de diferencia guassiana, bastante resistente a
		// cambios de tamaño, rotaciones y reposicionamiento
		DoGSIFTEngine engine = new DoGSIFTEngine();

		// Se extraen los Keypoints de cada imagen.
		LocalFeatureList<Keypoint> queryKeypoints = engine.findFeatures(query
				.flatten());
		LocalFeatureList<Keypoint> targetKeypoints = engine.findFeatures(target
				.flatten());

		// The challenge in comparing Keypoints is trying to figure out which
		// Keypoints match between Keypoints from some
		// query image and those from some target. The most basic approach is to
		// take a given Keypoint in the query and
		// find the Keypoint that is closest in the target. A minor improvement
		// on top of this is to disregard those
		// points which match well with MANY other points in the target. Such
		// point are considered non-descriptive.
		// Matching can be achieved in OpenIMAJ using the BasicMatcher. Next
		// we’ll construct and setup such a matcher:
		LocalFeatureMatcher<Keypoint> matcher = new BasicMatcher<Keypoint>(80);
		matcher.setModelFeatures(queryKeypoints);
		matcher.findMatches(targetKeypoints);
		
		if (esRobusto){
			
			RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(
					5.0, 1500, new RANSAC.PercentageInliersStoppingCondition(0.5));
			matcher = new ConsistentLocalFeatureMatcher2d<Keypoint>(
					new FastBasicKeypointMatcher<Keypoint>(8), modelFitter);
			
			matcher.setModelFeatures(queryKeypoints);
			matcher.findMatches(targetKeypoints);
		}

		MBFImage consistentMatches = MatchingUtilities.drawMatches(query,
				target, matcher.getMatches(), RGBColour.RED);
		
		JOptionPane.showMessageDialog(null, "Cantidad de coincidencias entre descriptores: " + String.valueOf(matcher.getMatches().size()));
		
		DisplayUtilities.display(consistentMatches);
	}

}
