# EchoStream Project Report

1) Problem Statement:
The digital audio ecosystem requires reliable systems that can manage not only media files, but also ownership, legal rights, financial distribution, and compliance-oriented approval workflows. Traditional content systems often treat these concerns as isolated modules, leading to fragmented processing, manual errors, delayed approvals, and disputes in royalty computation.

EchoStream addresses this challenge by providing a unified AI-powered audio content and rights management platform that supports the complete lifecycle of an audio asset. From ingestion and quality validation to licensing, royalty computation, and approval state transitions, the system offers a structured, traceable, and extensible flow built with OOAD principles. The objective is to ensure that creators, rights administrators, and auditors can collaborate through a consistent process where each role has clear responsibilities and data integrity is maintained throughout.

2) Key Features:
- **Audio Ingestion and Signal Validation**
	- Supports ingestion of multiple track types (music and podcast).
	- Captures required metadata such as title, duration, source path, and creator identity.
	- Displays validation-oriented status in the ingestion dashboard to support quick quality assessment.

- **Copyright and Digital License Management**
	- Enables license issuance for tracks with type, territory, and validity windows.
	- Tracks latest license state for each audio asset.
	- Supports renewal and revocation workflows from the rights management dashboard.

- **Royalty Distribution Engine**
	- Uses configurable royalty strategies to compute payouts based on usage and revenue.
	- Supports fixed-rate-per-stream and percentage revenue-share calculations.
	- Maintains transaction history for financial traceability.

- **Audit and Approval Workflow**
	- Manages lifecycle progression from upload through validation and audit stages.
	- Provides explicit approve/reject actions for auditor-controlled release decisions.
	- Ensures only permitted state transitions occur via the State Pattern.

- **Strict MVC User Experience**
	- Server-side rendered views using Thymeleaf templates.
	- Dedicated module-oriented dashboards for each team vertical.
	- Consistent navigation and layout through shared template fragments.

3) Models:
- **User (abstract base entity)**
	- Represents common profile attributes such as name, email, and role.
	- Acts as the inheritance root for actor-specific entities.

- **ContentCreator (extends User)**
	- Represents creators who upload content.
	- Holds one-to-many relation with uploaded tracks.

- **QualityAuditor (extends User)**
	- Represents reviewers responsible for audit decisions.
	- Linked to assigned tracks in workflow stages.

- **AudioTrack (abstract entity)**
	- Core domain object for all audio content.
	- Stores track metadata, lifecycle state, creator association, and linked licenses/payments.

- **MusicTrack (extends AudioTrack)**
	- Specialized track type for music-specific metadata (album/genre/ISRC where applicable).

- **PodcastTrack (extends AudioTrack)**
	- Specialized track type for podcast metadata (episode/host/show details).

- **DigitalLicense**
	- Encapsulates legal usage configuration per track.
	- Includes code, type, territory, effective dates, and link to track.

- **PaymentTransaction**
	- Records royalty payout transactions with amount, currency, status, date, and beneficiary.

These models are mapped through JPA with inheritance and relational annotations to preserve an object-oriented domain model while maintaining normalized relational persistence.

4) Use Case Diagram:
Placeholder: [Insert Use Case Diagram Image Here]

Use case interpretation guidance:
- **Actors**: Content Creator, Quality Auditor, Rights Manager, Finance/Admin.
- **Primary use cases**: Upload Track, Validate Track, Issue/Renew/Revoke License, Calculate Royalty, Approve/Reject Track, Create Payment Transaction.
- **System boundary**: EchoStream platform, including dashboards, services, and persistence.

5) Class Diagram:
Placeholder: [Insert Class Diagram Image Here]

Class diagram interpretation guidance:
- Show inheritance (`User -> ContentCreator / QualityAuditor`, `AudioTrack -> MusicTrack / PodcastTrack`).
- Show aggregation/association (`AudioTrack -> DigitalLicense`, `AudioTrack -> PaymentTransaction`).
- Show service-to-repository dependencies and pattern classes (Factory/Strategy/State/Adapter).

6) State Diagram:
Placeholder: [Insert State Diagram Image Here]

State model (implemented):
- Uploaded -> Validating -> PendingAudit -> Released
- Uploaded/Validating/PendingAudit -> Rejected (based on decision points)

This state machine ensures governance, preventing invalid transitions and preserving process correctness.

7) Activity Diagrams:
Placeholder: [Insert Activity Diagram Image(s) Here]

Suggested activity flows to present:
- Ingestion and metadata normalization flow
- License issuance and renewal flow
- Royalty calculation and payout flow
- Audit queue decision flow (approve/reject)

8) Design Principles, and Design Patterns:
The implementation follows OOAD-focused architecture with clear layering and SOLID-oriented responsibilities. Each module is decomposed into Controller, Service, Repository, and Model responsibilities so that business logic remains testable and independent of UI rendering details. The code intentionally uses four required design patterns to satisfy both extensibility and academic rubric alignment.

9) MVC Architecture used? Yes/No
Yes

Evidence of MVC usage:
- **Model**: Domain entities and repositories for persistence.
- **View**: Thymeleaf templates in `src/main/resources/templates`.
- **Controller**: MVC controllers under `controller/mvc` handling navigation, model binding, and form submissions.

REST controllers may exist for API operations, but primary user flow is through server-rendered MVC pages.

10) Design Principles
- Single Responsibility Principle (SRP)
- Open/Closed Principle (OCP)
- Separation of Concerns
- Dependency Inversion via Spring dependency injection

- Interface-oriented service boundaries
- High cohesion within module-specific verticals

10.1) <Explanation of principle usage in the project>
- **SRP**: Each layer has one clear responsibility.
	- Controllers handle route mapping and model transfer only.
	- Services implement use-case logic.
	- Repositories handle data access.
	- Templates handle presentation.

- **OCP**: New behavior can be introduced through new concrete implementations.
	- Additional royalty methods can be added as new Strategy implementations.
	- New lifecycle states can be introduced via State implementations.
	- New metadata sources can be integrated via Adapter implementations.

- **Separation of Concerns**:
	- Domain logic is isolated from UI and persistence concerns.
	- MVC views do not embed business computations.
	- Business rules remain centralized in service/domain layers.

- **Dependency Inversion**:
	- Spring DI manages component wiring.
	- Services depend on repository abstractions rather than concrete DB logic.
	- Improves maintainability and supports modular testing.

- **Cohesion and Modularity**:
	- Four vertical modules map to team ownership and feature boundaries.
	- Each vertical remains functionally focused, reducing accidental coupling.

11) Design Patterns
- Factory Method
- Strategy
- State
- Adapter

11.1) <Explanation of pattern usage in the project>
- **Factory Method (Creational)**
	- Problem solved: Object construction differs by track type.
	- Usage: Concrete factories create `MusicTrack` and `PodcastTrack` through a common creation contract.
	- Benefit: Creation logic is centralized, reusable, and insulated from callers.

- **Strategy (Behavioral)**
	- Problem solved: Royalty formulas vary by business rule.
	- Usage: Fixed-rate and revenue-share computations are encapsulated as interchangeable strategies selected at runtime.
	- Benefit: Royalty engine remains extensible without modifying existing orchestrators.

- **State (Behavioral)**
	- Problem solved: Track lifecycle rules require controlled transitions.
	- Usage: Each lifecycle state object defines legal transitions and rejects invalid ones.
	- Benefit: Workflow correctness is enforced structurally instead of scattered conditional logic.

- **Adapter (Structural)**
	- Problem solved: Metadata arrives in different external formats.
	- Usage: Source-specific adapters transform metadata into a unified internal DTO.
	- Benefit: Ingestion service remains format-agnostic and easier to maintain.

12) Github link to the Codebase: (repository should be public)
https://github.com/Sreehas125/EchoStream

Screenshots

UI:
Placeholder: [Insert UI Screenshot(s) Here]

Additional screenshot placeholders (recommended for report quality):
- Placeholder: [Insert Ingestion Dashboard Screenshot]
- Placeholder: [Insert Rights Management Screenshot]
- Placeholder: [Insert Royalty Report Screenshot]
- Placeholder: [Insert Audit Queue Screenshot]


Individual contributions of the team members:

| Name | Module worked on |
|------|-------------------|
| Sri Kalyan Ippili | Audio Ingestion and signal validation |
| Sreehas Adusumilli | Copyright and licensing engine |
| Srirama V Swamy | Royalty Distribution Logic|
| Shubhang S | Content Approval Workflow |

Contribution elaboration:
- **Sri Kalyan Ippili**
	- Implemented ingestion flow and upload handling.
	- Supported signal validation display and ingestion status workflow.

- **Sreehas Adusumilli**
	- Implemented rights and license lifecycle operations.
	- Added license issue/renew/revoke logic and rights dashboard behavior.

- **Srirama V Swamy**
	- Implemented royalty strategy computations for payout models.
	- Built royalty report flow and transaction-oriented financial outputs.

- **Shubhang S**
	- Implemented audit queue controls and approval transitions.
	- Integrated state-driven release/reject actions in workflow pages.

---

## Conclusion
EchoStream demonstrates a complete OOAD-oriented implementation of an audio content platform with strict MVC architecture and pattern-driven domain logic. The system is modular, role-oriented, and extensible, with explicit support for creation variability (Factory), computational variability (Strategy), lifecycle governance (State), and data compatibility (Adapter). The resulting architecture is suitable for academic evaluation and also provides a practical foundation for future production-scale enhancements such as authentication, audit logging, advanced analytics, and cloud-based media processing.
