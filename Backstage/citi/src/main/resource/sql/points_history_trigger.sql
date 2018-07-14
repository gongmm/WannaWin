DELIMITER $$
CREATE TRIGGER points_update AFTER UPDATE
ON m_card
FOR EACH ROW
BEGIN
	DECLARE _userID VARCHAR(45) DEFAULT NEW.UserID;
    DECLARE _cardID VARCHAR(45) DEFAULT NEW.CardID;
    DECLARE _merchantID VARCHAR(45) DEFAULT NEW.MerchantID;
	DECLARE _diff_points INT DEFAULT NEW.points - OLD.points;
    DECLARE _proportion VARCHAR(45);
    SELECT proportion FROM merchant WHERE MerchantID = _merchantID INTO _proportion;
	IF _diff_points > 0 THEN
		INSERT INTO points_history VALUES(_userID, _cardID, _diff_points, _proportion * _diff_points, "GAIN", NOW());
	ELSE
		INSERT INTO points_history VALUES(_userID, _cardID, 0 - _diff_points, 0 - _proportion * _diff_points, "EXCHANGE", NOW());
    END IF;
END;$$
