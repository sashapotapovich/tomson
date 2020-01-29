package com.common.model;

import com.common.annotation.CrearecNotSql;
import lombok.*;

import java.io.Serializable;
import java.rmi.Remote;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Entity implements Remote, Serializable {
	@CrearecNotSql
	private static final long serialVersionUID = 5865358553269068913L;

	@CrearecNotSql
	private Long id;

	@CrearecNotSql
	private String errorMessage;
}
