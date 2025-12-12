import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import "./App.css"
import App from './App.jsx'
import { Amplify } from "aws-amplify";
import awsExports from "./aws-exports";
import { Toaster } from "react-hot-toast";

/**
 * @file main.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * Configures the AWS Amplify library using credentials exported from aws-exports.js.
 * This sets up connections to AWS services (e.g., Cognito, API Gateway, DynamoDB).
 * 
 */
Amplify.configure(awsExports);

/**
 * Selects the root DOM element and renders the main React application.
 * Uses the modern React 18+ {@code createRoot} API for concurrent rendering.
 */
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
    {/* Global Toaster component for displaying notifications throughout the application */}
    <Toaster position="top-right" />
  </StrictMode>,
)