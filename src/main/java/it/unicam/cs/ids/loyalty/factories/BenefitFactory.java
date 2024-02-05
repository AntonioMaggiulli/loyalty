package it.unicam.cs.ids.loyalty.factories;

import java.util.Date;

import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.Cashback;
import it.unicam.cs.ids.loyalty.model.Coupon;
import it.unicam.cs.ids.loyalty.model.Level;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.model.PointsReward;
import it.unicam.cs.ids.loyalty.model.Reward;

//Factory class
public class BenefitFactory {

	/**
	 * Creates a Benefit object based on the specified type and provided parameters.
	 * 
	 * @param type The type of Benefit to create. Must be one of the predefined strings such as "COUPON", "CASHBACK", "REWARD", "POINTS_REWARD".
	 * @param name The name of the Benefit.
	 * @param description The description of the Benefit.
	 * @param pointsRequired The points required to redeem the Benefit.
	 * @param offeringMerchant The merchant offering the Benefit.
	 * @param loyaltyProgram The loyalty program associated with the Benefit.
	 * @param associatedLevel The level associated with the Benefit.
	 * @param additionalParams Additional parameters required for creating specific types of Benefits. The order and type of parameters should be as follows:
	 *                        - For "COUPON": (Date) expirationDate
	 *                        - For "CASHBACK": (Double) cashBackRate
	 *                        - For "REWARD": (Integer) specific quantity available for the type of Reward
	 *                        - For "POINTS_REWARD": (Integer) earnsPoints, (Double) moneySpent
	 * @return An instance of Benefit of the specified type, configured with the provided parameters.
	 * @throws IllegalArgumentException if the type of Benefit is not supported or if the additional parameters are incorrect.
	 */
	public static Benefit createBenefit(
		    String type,
		    String name,
		    String description,
		    int pointsRequired,
		    Merchant offeringMerchant,
		    LoyaltyProgram loyaltyProgram,
		    Level associatedLevel,
		    Object... additionalParams // Parametri aggiuntivi, come la data di scadenza o il tasso di cashback
		) {
		    Benefit benefit;
		    switch (type) {
		        case "COUPON":
		            Date expirationDate = (Date) additionalParams[0];
		            benefit = new Coupon();
		            ((Coupon) benefit).setExpiringDate(expirationDate);
		            break;
		        case "CASHBACK":
		            double cashBackRate = (Double) additionalParams[0];
		            Double minSpent= (Double) additionalParams[1];
		            benefit = new Cashback();
		            ((Cashback) benefit).setCashBackRate(cashBackRate);
		            ((Cashback) benefit).setMinSpent(minSpent);
		            break;
		        case "REWARD":
		        	int qtyAvailable = (Integer) additionalParams[0];
		        	benefit = new Reward();
		            ((Reward) benefit).setQty(qtyAvailable);
		            break;
		        case "POINTS_REWARD":
		            int earnsPoints = (Integer) additionalParams[0]; 
		            double moneySpent = (Double) additionalParams[1];
		            benefit = new PointsReward();
		            ((PointsReward) benefit).setEarnsPoints(earnsPoints);
		            ((PointsReward) benefit).setMoneySpent(moneySpent);
		            break;
		        default:
		            throw new IllegalArgumentException("Tipo di Benefit non supportato.");
		    }
		    
		    benefit.setType(type);
		    benefit.setName(name);
		    benefit.setDescription(description);
		    benefit.setPointsRequired(pointsRequired);
		    benefit.setOfferingMerchant(offeringMerchant);
		    benefit.setLoyaltyProgram(loyaltyProgram);
		    benefit.setAssociatedLevel(associatedLevel);
		    
		    return benefit;
		}
}

