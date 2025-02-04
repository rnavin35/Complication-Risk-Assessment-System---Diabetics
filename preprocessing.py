import pandas as pd
import numpy as np
import json

# Load Datasets
diabetes_indicators = pd.read_csv('diabetes_health_indicators.csv')
drug_recommendations = pd.read_csv('diabetes_drug_recommendations.csv')

def preprocess_indicators_dataset(df):
    # Clean and transform data
    df['risk_score'] = (
        df['Diabetes_binary'] * 0.5 +
        df['HighBP'] * 0.3 +
        df['HighChol'] * 0.2
    )

    # Create feature embeddings
    features = ['Age', 'BMI', 'HbA1c', 'Glucose']
    from sklearn.preprocessing import StandardScaler
    scaler = StandardScaler()
    df[features] = scaler.fit_transform(df[features])

    return df

def preprocess_drug_recommendations(df):
    # Transform drug effectiveness data
    df['effectiveness_score'] = (
        df['patient_response'] * df['drug_dosage'] /
        df['side_effects_severity']
    )

    return df

# Prepare Neo4j importable JSON
def prepare_neo4j_import(df, output_file):
    records = df.to_dict('records')
    with open(output_file, 'w') as f:
        json.dump(records, f)

# Process and export
indicators_processed = preprocess_indicators_dataset(diabetes_indicators)
drug_processed = preprocess_drug_recommendations(drug_recommendations)

prepare_neo4j_import(indicators_processed, 'diabetes_indicators.json')
prepare_neo4j_import(drug_processed, 'drug_recommendations.json')